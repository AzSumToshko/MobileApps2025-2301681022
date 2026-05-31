package com.example.automarket

import com.example.automarket.data.local.entity.CarEntity
import org.junit.Assert.*
import org.junit.Test

class CarEntityTest {

    private fun sampleCar(
        brand: String = "BMW",
        model: String = "X5",
        year: Int = 2021,
        price: Double = 50000.0,
        mileage: Int = 60000,
        fuelType: String = "Дизел",
        transmission: String = "Автоматик",
        bodyType: String = "SUV",
        color: String = "Черен",
        engineSize: String = "3.0",
        powerHp: Int = 265,
        description: String = "",
        location: String = "София"
    ) = CarEntity(
        brand = brand, model = model, year = year, price = price,
        mileage = mileage, fuelType = fuelType, transmission = transmission,
        bodyType = bodyType, color = color, engineSize = engineSize,
        powerHp = powerHp, description = description, location = location
    )

    @Test
    fun newCar_defaultIdIsZero() {
        val car = sampleCar()
        assertEquals(0, car.id)
    }

    @Test
    fun newCar_isNotFavoriteByDefault() {
        val car = sampleCar()
        assertFalse(car.isFavorite)
    }

    @Test
    fun newCar_photoUriIsNullByDefault() {
        val car = sampleCar()
        assertNull(car.photoUri)
    }

    @Test
    fun newCar_createdAtTimestampIsRecent() {
        val before = System.currentTimeMillis()
        val car = sampleCar()
        val after = System.currentTimeMillis()
        assertTrue(car.createdAt in before..after)
    }

    @Test
    fun car_priceIsPositive() {
        val car = sampleCar(price = 35000.0)
        assertTrue(car.price > 0)
    }

    @Test
    fun car_copyAsFavorite_keepsBrandAndModel() {
        val car = sampleCar(brand = "Audi", model = "A4")
        val favorited = car.copy(isFavorite = true)
        assertTrue(favorited.isFavorite)
        assertEquals("Audi", favorited.brand)
        assertEquals("A4", favorited.model)
    }

    @Test
    fun car_fieldsMatchConstructorArgs() {
        val car = sampleCar(
            brand = "Toyota", model = "Corolla", year = 2019,
            price = 22000.0, mileage = 80000, location = "Варна"
        )
        assertEquals("Toyota", car.brand)
        assertEquals("Corolla", car.model)
        assertEquals(2019, car.year)
        assertEquals(22000.0, car.price, 0.0)
        assertEquals(80000, car.mileage)
        assertEquals("Варна", car.location)
    }

    @Test
    fun car_copyWithPhotoUri_setsUri() {
        val car = sampleCar()
        val withPhoto = car.copy(photoUri = "content://media/external/images/1")
        assertNotNull(withPhoto.photoUri)
        assertEquals("content://media/external/images/1", withPhoto.photoUri)
    }
}
