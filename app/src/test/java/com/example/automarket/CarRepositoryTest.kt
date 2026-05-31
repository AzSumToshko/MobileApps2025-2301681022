package com.example.automarket

import com.example.automarket.data.local.dao.CarDao
import com.example.automarket.data.local.entity.CarEntity
import com.example.automarket.data.repository.CarRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CarRepositoryTest {

    private lateinit var carDao: CarDao
    private lateinit var repository: CarRepository

    private val testCar = CarEntity(
        id = 1,
        brand = "Mercedes",
        model = "C200",
        year = 2020,
        price = 45000.0,
        mileage = 35000,
        fuelType = "Бензин",
        transmission = "Автоматик",
        bodyType = "Седан",
        color = "Сребрист",
        engineSize = "2.0",
        powerHp = 184,
        description = "Отлично състояние",
        location = "Пловдив"
    )

    @Before
    fun setUp() {
        carDao = mock()
        whenever(carDao.getAllCars()).thenReturn(flowOf(emptyList()))
        whenever(carDao.getFavoriteCars()).thenReturn(flowOf(emptyList()))
        repository = CarRepository(carDao)
    }

    @Test
    fun allCars_exposesFlowFromDao() {
        val cars = listOf(testCar)
        whenever(carDao.getAllCars()).thenReturn(flowOf(cars))
        repository = CarRepository(carDao)
        assertNotNull(repository.allCars)
    }

    @Test
    fun favoriteCars_exposesFlowFromDao() {
        whenever(carDao.getFavoriteCars()).thenReturn(flowOf(listOf(testCar)))
        repository = CarRepository(carDao)
        assertNotNull(repository.favoriteCars)
    }

    @Test
    fun insertCar_delegatesToDao() = runTest {
        whenever(carDao.insertCar(testCar)).thenReturn(1L)
        repository.insertCar(testCar)
        verify(carDao).insertCar(testCar)
    }

    @Test
    fun deleteCar_delegatesToDao() = runTest {
        repository.deleteCar(testCar)
        verify(carDao).deleteCar(testCar)
    }

    @Test
    fun updateCar_delegatesToDao() = runTest {
        repository.updateCar(testCar)
        verify(carDao).updateCar(testCar)
    }

    @Test
    fun toggleFavorite_delegatesToDao() = runTest {
        repository.toggleFavorite(1, true)
        verify(carDao).toggleFavorite(1, true)
    }

    @Test
    fun getCarById_returnsCarFromDao() = runTest {
        whenever(carDao.getCarById(1)).thenReturn(testCar)
        val result = repository.getCarById(1)
        assertEquals(testCar, result)
        assertEquals("Mercedes", result?.brand)
    }

    @Test
    fun getCarById_returnsNullWhenNotFound() = runTest {
        whenever(carDao.getCarById(99)).thenReturn(null)
        val result = repository.getCarById(99)
        assertNull(result)
    }
}
