package com.example.automarket.data.repository

import com.example.automarket.data.local.dao.CarDao
import com.example.automarket.data.local.entity.CarEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepository @Inject constructor(private val carDao: CarDao) {

    val allCars: Flow<List<CarEntity>> = carDao.getAllCars()

    val favoriteCars: Flow<List<CarEntity>> = carDao.getFavoriteCars()

    fun searchCars(brand: String?, maxPrice: Double?, fuelType: String?): Flow<List<CarEntity>> =
        carDao.searchCars(brand, maxPrice, fuelType)

    suspend fun insertCar(car: CarEntity): Long = carDao.insertCar(car)

    suspend fun updateCar(car: CarEntity) = carDao.updateCar(car)

    suspend fun deleteCar(car: CarEntity) = carDao.deleteCar(car)

    suspend fun toggleFavorite(id: Int, isFavorite: Boolean) = carDao.toggleFavorite(id, isFavorite)

    suspend fun getCarById(id: Int): CarEntity? = carDao.getCarById(id)
}
