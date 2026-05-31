package com.example.automarket.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.automarket.data.repository.CarRepository
import com.example.automarket.ui.screens.components.CarPreview
import com.example.automarket.ui.screens.components.toPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    val favoriteCars: StateFlow<List<CarPreview>> = repository.favoriteCars
        .map { list -> list.map { it.toPreview() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun removeFromFavorites(carId: Int) = viewModelScope.launch {
        repository.toggleFavorite(carId, false)
    }
}
