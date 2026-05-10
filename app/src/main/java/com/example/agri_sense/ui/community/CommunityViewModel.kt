package com.example.agri_sense.ui.community
 
import android.net.Uri

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.models.Discussion
import com.example.agri_sense.data.models.PestAlert
import com.example.agri_sense.data.repository.DiscussionRepository
import com.example.agri_sense.data.repository.FarmerRepository
import com.example.agri_sense.data.repository.PestAlertRepository
import com.example.agri_sense.data.network.CommunityApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val discussionRepository: DiscussionRepository,
    private val pestRepository: PestAlertRepository,
    private val farmerRepository: FarmerRepository,
    private val communityApi: CommunityApi
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
 
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private val _farmerCount = MutableStateFlow("12,450+")
    val farmerCount: StateFlow<String> = _farmerCount.asStateFlow()

    val unreadPestCount: StateFlow<Int> = pestRepository.unreadCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _allDiscussions = MutableStateFlow<List<Discussion>>(emptyList())

    val filteredDiscussions: StateFlow<List<Discussion>> = combine(
        _allDiscussions, _searchQuery
    ) { discussions, query ->
        if (query.isEmpty()) discussions
        else discussions.filter {
            it.question.contains(query, ignoreCase = true) ||
            it.tags.contains(query, ignoreCase = true) ||
            it.authorDistrict.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pestAlerts: StateFlow<List<PestAlert>> = pestRepository.allAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    init {
        viewModelScope.launch {
            pestRepository.seedIfEmpty()
            discussionRepository.seedIfEmpty()
            fetchFarmerCount()
        }
        observeDiscussions()
    }

    private fun observeDiscussions() {
        viewModelScope.launch {
            discussionRepository.allDiscussions.collect { list ->
                _allDiscussions.value = list
            }
        }
    }

    fun setSearchQuery(query: String) { _searchQuery.value = query }
 
    fun pickImage(uri: Uri?) { _selectedImageUri.value = uri }
 
    fun clearSelectedImage() { _selectedImageUri.value = null }

    fun markAllAlertsAsRead() {
        viewModelScope.launch {
            pestRepository.markAllRead()
        }
    }

    fun postQuestion(question: String) {
        viewModelScope.launch {
            val farmer = farmerRepository.getFarmerOnce()
            val imageUrl = _selectedImageUri.value?.toString() ?: ""
            
            discussionRepository.postQuestion(
                question = question,
                authorName = farmer?.name ?: "Anonymous Farmer",
                authorDistrict = farmer?.district ?: "Malawi",
                authorCrop = farmer?.cropsGrown?.split(",")?.firstOrNull() ?: "",
                imageUrl = imageUrl
            )
            clearSelectedImage()
        }
    }

    fun likeDiscussion(id: String) {
        viewModelScope.launch { discussionRepository.likeDiscussion(id) }
    }

    fun addComment(id: String, comment: String) {
        viewModelScope.launch { discussionRepository.addComment(id, comment) }
    }

    fun markPestAlertRead(id: String) {
        viewModelScope.launch { pestRepository.markAsRead(id) }
    }

    fun markAllPestAlertsRead() {
        viewModelScope.launch { pestRepository.markAllRead() }
    }

    private fun fetchFarmerCount() {
        viewModelScope.launch {
            try {
                val response = communityApi.getFarmerCount()
                _farmerCount.value = "${java.text.NumberFormat.getInstance().format(response.count)}+"
            } catch (e: Exception) {
                // Keep default
            }
        }
    }
}
