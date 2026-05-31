package com.example.automarket.ui.screens.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.automarket.data.local.entity.CarEntity
import com.example.automarket.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostAdViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    fun insertCar(car: CarEntity) = viewModelScope.launch {
        repository.insertCar(car)
    }
}
