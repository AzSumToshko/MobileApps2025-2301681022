package com.example.automarket.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.automarket.data.local.entity.CarEntity
import com.example.automarket.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarDetailViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    private val _car = MutableStateFlow<CarEntity?>(null)
    val car: StateFlow<CarEntity?> = _car.asStateFlow()

    fun loadCar(id: Int) = viewModelScope.launch {
        _car.value = repository.getCarById(id)
    }

    fun deleteCar(onDeleted: () -> Unit) = viewModelScope.launch {
        _car.value?.let {
            repository.deleteCar(it)
            onDeleted()
        }
    }

    fun toggleFavorite() = viewModelScope.launch {
        _car.value?.let { car ->
            val newState = !car.isFavorite
            repository.toggleFavorite(car.id, newState)
            _car.value = car.copy(isFavorite = newState)
        }
    }
}
