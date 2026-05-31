package com.example.automarket.ui.screens.home

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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    val cars: StateFlow<List<CarPreview>> = repository.allCars
        .map { list -> list.map { it.toPreview() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
