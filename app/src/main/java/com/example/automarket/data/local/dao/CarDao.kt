package com.example.automarket.data.local.dao

import androidx.room.*
import com.example.automarket.data.local.entity.CarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    @Query("SELECT * FROM cars ORDER BY createdAt DESC")
    fun getAllCars(): Flow<List<CarEntity>>

    @Query("SELECT * FROM cars WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteCars(): Flow<List<CarEntity>>

    @Query("SELECT * FROM cars WHERE id = :id")
    suspend fun getCarById(id: Int): CarEntity?

    @Query("""
        SELECT * FROM cars WHERE
        (:brand IS NULL OR brand = :brand) AND
        (:maxPrice IS NULL OR price <= :maxPrice) AND
        (:fuelType IS NULL OR fuelType = :fuelType)
        ORDER BY createdAt DESC
    """)
    fun searchCars(brand: String?, maxPrice: Double?, fuelType: String?): Flow<List<CarEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: CarEntity): Long

    @Update
    suspend fun updateCar(car: CarEntity)

    @Delete
    suspend fun deleteCar(car: CarEntity)

    @Query("UPDATE cars SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Int, isFavorite: Boolean)
}
