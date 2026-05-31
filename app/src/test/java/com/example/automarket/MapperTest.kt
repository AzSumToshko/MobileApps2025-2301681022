package com.example.automarket

import com.example.automarket.data.local.entity.CarEntity
import com.example.automarket.ui.screens.components.brandColor
import com.example.automarket.ui.screens.components.toPreview
import org.junit.Assert.*
import org.junit.Test

class MapperTest {

    private val car = CarEntity(
        id = 5,
        brand = "BMW", model = "X5", year = 2021,
        price = 89500.0, mileage = 65000,
        fuelType = "Дизел", transmission = "Автоматик",
        bodyType = "SUV", color = "Черен",
        engineSize = "3.0", powerHp = 265,
        description = "Добро описание", location = "Пловдив",
        isFavorite = false
    )

    @Test
    fun toPreview_setsCorrectFullTitle() {
        assertEquals("BMW X5", car.toPreview().fullTitle)
    }

    @Test
    fun toPreview_preservesId() {
        assertEquals(5, car.toPreview().id)
    }

    @Test
    fun toPreview_setsYearAndMileage() {
        assertEquals("2021 • 65000 км", car.toPreview().yearMileage)
    }

    @Test
    fun toPreview_setsFuelAndTransmission() {
        assertEquals("Дизел • Автоматик", car.toPreview().fuelTransmission)
    }

    @Test
    fun toPreview_formatsPrice() {
        assertEquals("89500 лв.", car.toPreview().price)
    }

    @Test
    fun toPreview_setsLocation() {
        assertEquals("Пловдив", car.toPreview().location)
    }

    @Test
    fun toPreview_propagatesFavoriteTrue() {
        assertTrue(car.copy(isFavorite = true).toPreview().isFavorite)
    }

    @Test
    fun toPreview_propagatesFavoriteFalse() {
        assertFalse(car.copy(isFavorite = false).toPreview().isFavorite)
    }

    @Test
    fun toPreview_colorPlaceholderIsNotNull() {
        assertNotNull(car.toPreview().colorPlaceholder)
    }

    @Test
    fun brandColor_sameBrandAlwaysReturnsSameColor() {
        assertEquals(brandColor("Toyota"), brandColor("Toyota"))
    }

    @Test
    fun brandColor_isNotNull() {
        assertNotNull(brandColor("Audi"))
    }
}
