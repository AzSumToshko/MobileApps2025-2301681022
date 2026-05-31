package com.example.automarket.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.automarket.data.repository.CarRepository
import com.example.automarket.ui.screens.components.CarPreview
import com.example.automarket.ui.screens.components.toPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class SearchParams(
    val brand: String? = null,
    val maxPrice: Double? = null,
    val fuelType: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    private val _params = MutableStateFlow(SearchParams())

    @OptIn(ExperimentalCoroutinesApi::class)
    val results: StateFlow<List<CarPreview>> = _params
        .flatMapLatest { p ->
            repository.searchCars(p.brand, p.maxPrice, p.fuelType)
                .map { list -> list.map { it.toPreview() } }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun search(brand: String?, maxPrice: Double?, fuelType: String?) {
        _params.value = SearchParams(brand, maxPrice, fuelType)
    }
}
