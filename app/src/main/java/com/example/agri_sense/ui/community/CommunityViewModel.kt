package com.example.agri_sense.ui.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.models.Discussion
import com.example.agri_sense.data.models.PestAlert
import com.example.agri_sense.data.repository.DiscussionRepository
import com.example.agri_sense.data.repository.FarmerRepository
import com.example.agri_sense.data.repository.PestAlertRepository
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
    private val farmerRepository: FarmerRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

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

    val unreadPestCount: StateFlow<Int> = pestRepository.unreadCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        viewModelScope.launch {
            pestRepository.seedIfEmpty()
            discussionRepository.seedIfEmpty()
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

    fun postQuestion(question: String) {
        viewModelScope.launch {
            val farmer = farmerRepository.getFarmerOnce()
            discussionRepository.postQuestion(
                question = question,
                authorName = farmer?.name ?: "Anonymous Farmer",
                authorDistrict = farmer?.district ?: "Malawi",
                authorCrop = farmer?.cropsGrown?.split(",")?.firstOrNull() ?: ""
            )
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
}
