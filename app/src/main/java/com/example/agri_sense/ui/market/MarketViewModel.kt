package com.example.agri_sense.ui.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agri_sense.data.models.MarketPrice
import com.example.agri_sense.data.repository.MarketRepository
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
class MarketViewModel @Inject constructor(
    private val marketRepository: MarketRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedRegion = MutableStateFlow("All")
    val selectedRegion: StateFlow<String> = _selectedRegion.asStateFlow()

    private val _allPrices = MutableStateFlow<List<MarketPrice>>(emptyList())

    val filteredPrices: StateFlow<List<MarketPrice>> = combine(
        _allPrices, _searchQuery, _selectedRegion
    ) { prices, query, region ->
        prices.filter { price ->
            val matchesQuery = query.isEmpty() ||
                price.cropName.contains(query, ignoreCase = true) ||
                price.cropNameChichewa.contains(query, ignoreCase = true) ||
                price.marketName.contains(query, ignoreCase = true)
            val matchesRegion = region == "All" || price.region == region
            matchesQuery && matchesRegion
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            marketRepository.seedIfEmpty()
            marketRepository.syncLivePrices()
        }
        observePrices()
    }

    private fun observePrices() {
        viewModelScope.launch {
            marketRepository.allPrices.collect { prices ->
                _allPrices.value = prices
            }
        }
    }

    fun setPriceAlert(farmerId: String, cropName: String, targetPrice: Double) {
        viewModelScope.launch {
            marketRepository.setPriceAlert(farmerId, cropName, targetPrice)
        }
    }

    fun setSearchQuery(query: String) { _searchQuery.value = query }

    fun setSelectedRegion(region: String) { _selectedRegion.value = region }

    fun getTopGainers(): List<MarketPrice> =
        _allPrices.value.sortedByDescending { it.trendPercent }.take(3)

    fun getPriceForCrop(cropName: String): MarketPrice? =
        _allPrices.value.firstOrNull { it.cropName.equals(cropName, ignoreCase = true) }
}
