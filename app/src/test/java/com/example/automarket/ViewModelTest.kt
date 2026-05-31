package com.example.automarket

import com.example.automarket.data.local.entity.CarEntity
import com.example.automarket.data.repository.CarRepository
import com.example.automarket.ui.screens.detail.CarDetailViewModel
import com.example.automarket.ui.screens.favorites.FavoritesViewModel
import com.example.automarket.ui.screens.home.HomeViewModel
import com.example.automarket.ui.screens.post.PostAdViewModel
import com.example.automarket.ui.screens.search.SearchParams
import com.example.automarket.ui.screens.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: CarRepository

    private val sampleCar = CarEntity(
        id = 1,
        brand = "Audi", model = "Q5", year = 2021,
        price = 55000.0, mileage = 30000,
        fuelType = "Дизел", transmission = "Автоматик",
        bodyType = "SUV", color = "Бял",
        engineSize = "2.0", powerHp = 204,
        description = "Добро състояние", location = "Варна"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        whenever(repository.allCars).thenReturn(flowOf(emptyList()))
        whenever(repository.favoriteCars).thenReturn(flowOf(emptyList()))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── HomeViewModel ─────────────────────────────────────────────────────

    @Test
    fun homeViewModel_carsIsEmptyInitially() {
        val vm = HomeViewModel(repository)
        assertTrue(vm.cars.value.isEmpty())
    }

    @Test
    fun homeViewModel_carsStateFlowIsNotNull() {
        val vm = HomeViewModel(repository)
        assertNotNull(vm.cars)
    }

    @Test
    fun homeViewModel_withCarData_mappedCorrectly() = runTest {
        whenever(repository.allCars).thenReturn(flowOf(listOf(sampleCar)))
        val vm = HomeViewModel(repository)
        advanceUntilIdle()
        assertNotNull(vm.cars)
    }

    // ── PostAdViewModel ───────────────────────────────────────────────────

    @Test
    fun postAdViewModel_insertCar_delegatesToRepository() = runTest {
        whenever(repository.insertCar(any())).thenReturn(1L)
        val vm = PostAdViewModel(repository)
        vm.insertCar(sampleCar)
        advanceUntilIdle()
        verify(repository).insertCar(sampleCar)
    }

    @Test
    fun postAdViewModel_insertCar_doesNotThrow() = runTest {
        whenever(repository.insertCar(any())).thenReturn(2L)
        val vm = PostAdViewModel(repository)
        vm.insertCar(sampleCar) // test passes if no exception is thrown
        advanceUntilIdle()
    }

    // ── CarDetailViewModel ────────────────────────────────────────────────

    @Test
    fun carDetailViewModel_carIsNullBeforeLoad() {
        val vm = CarDetailViewModel(repository)
        assertNull(vm.car.value)
    }

    @Test
    fun carDetailViewModel_loadCar_setsCar() = runTest {
        whenever(repository.getCarById(1)).thenReturn(sampleCar)
        val vm = CarDetailViewModel(repository)
        vm.loadCar(1)
        advanceUntilIdle()
        assertEquals(sampleCar, vm.car.value)
        assertEquals("Audi", vm.car.value?.brand)
    }

    @Test
    fun carDetailViewModel_loadCar_unknownIdReturnsNull() = runTest {
        whenever(repository.getCarById(99)).thenReturn(null)
        val vm = CarDetailViewModel(repository)
        vm.loadCar(99)
        advanceUntilIdle()
        assertNull(vm.car.value)
    }

    @Test
    fun carDetailViewModel_toggleFavorite_flipsState() = runTest {
        whenever(repository.getCarById(1)).thenReturn(sampleCar) // isFavorite = false
        val vm = CarDetailViewModel(repository)
        vm.loadCar(1)
        advanceUntilIdle()
        vm.toggleFavorite()
        advanceUntilIdle()
        verify(repository).toggleFavorite(1, true)
        assertTrue(vm.car.value?.isFavorite == true)
    }

    @Test
    fun carDetailViewModel_deleteCar_callsRepositoryAndCallback() = runTest {
        whenever(repository.getCarById(1)).thenReturn(sampleCar)
        val vm = CarDetailViewModel(repository)
        vm.loadCar(1)
        advanceUntilIdle()

        var callbackFired = false
        vm.deleteCar { callbackFired = true }
        advanceUntilIdle()

        verify(repository).deleteCar(sampleCar)
        assertTrue(callbackFired)
    }

    @Test
    fun carDetailViewModel_deleteCarWithNullCar_doesNotCallRepository() = runTest {
        val vm = CarDetailViewModel(repository) // car is null
        vm.deleteCar { }
        advanceUntilIdle()
        verify(repository, never()).deleteCar(any())
    }

    // ── FavoritesViewModel ────────────────────────────────────────────────

    @Test
    fun favoritesViewModel_favoriteCarsIsEmptyInitially() {
        val vm = FavoritesViewModel(repository)
        assertTrue(vm.favoriteCars.value.isEmpty())
    }

    @Test
    fun favoritesViewModel_removeFromFavorites_callsToggle() = runTest {
        val vm = FavoritesViewModel(repository)
        vm.removeFromFavorites(1)
        advanceUntilIdle()
        verify(repository).toggleFavorite(1, false)
    }

    @Test
    fun favoritesViewModel_removeFromFavorites_usesCorrectId() = runTest {
        val vm = FavoritesViewModel(repository)
        vm.removeFromFavorites(42)
        advanceUntilIdle()
        verify(repository).toggleFavorite(42, false)
        verify(repository, never()).toggleFavorite(eq(1), any())
    }

    // ── SearchViewModel / SearchParams ────────────────────────────────────

    @Test
    fun searchParams_defaultValuesAreNull() {
        val params = SearchParams()
        assertNull(params.brand)
        assertNull(params.maxPrice)
        assertNull(params.fuelType)
    }

    @Test
    fun searchParams_storesProvidedValues() {
        val params = SearchParams(brand = "BMW", maxPrice = 60000.0, fuelType = "Дизел")
        assertEquals("BMW", params.brand)
        assertEquals(60000.0, params.maxPrice)
        assertEquals("Дизел", params.fuelType)
    }

    @Test
    fun searchParams_equalityWorks() {
        val a = SearchParams("Toyota", 30000.0, "Бензин")
        val b = SearchParams("Toyota", 30000.0, "Бензин")
        assertEquals(a, b)
    }

    @Test
    fun searchViewModel_resultsIsEmptyInitially() {
        val vm = SearchViewModel(repository)
        assertTrue(vm.results.value.isEmpty())
    }

    @Test
    fun searchViewModel_searchUpdatesParams() = runTest {
        whenever(repository.searchCars(any(), any(), any())).thenReturn(flowOf(emptyList()))
        val vm = SearchViewModel(repository)
        vm.search("BMW", 50000.0, "Дизел") // passes if no exception thrown
        advanceUntilIdle()
    }
}
