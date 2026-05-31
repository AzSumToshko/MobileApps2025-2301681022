package com.example.automarket.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val brand: String,
    val model: String,
    val year: Int,
    val price: Double,
    val mileage: Int,
    val fuelType: String,
    val transmission: String,
    val bodyType: String,
    val color: String,
    val engineSize: String,
    val powerHp: Int,
    val description: String,
    val location: String,
    val isFavorite: Boolean = false,
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
