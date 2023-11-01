package com.techchai.weatherapp.feature.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.techchai.weatherapp.core.network.response.CityQueryResponseItem
import com.techchai.weatherapp.data.Result
import com.techchai.weatherapp.data.WeatherRepositoryImpl
import com.techchai.weatherapp.data.datastore.AppDataStore
import com.techchai.weatherapp.data.location.LocationHelper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.io.InputStream
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SearhViewModel {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repositoryImpl = mockk<WeatherRepositoryImpl>(relaxed = true)
    private val locationHelper = mockk<LocationHelper>(relaxed = true)
    private val appDataStore = mockk<AppDataStore>(relaxed = true)

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        viewModel = SearchViewModel(repositoryImpl, locationHelper, appDataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSearchQueryResponseNotEmpty() = runTest {
        coEvery { repositoryImpl.searchWeather("") }.returns(
            Result.Success(getWeatherInfo("search.json"))
        )
        val weatherData = viewModel.uiState.value.queryCityList
        assertTrue(weatherData.isNotEmpty())
    }

    private fun getWeatherInfo(filename: String): CityQueryResponseItem {
        try {
            val json = File("$filename").readFile()
            return Gson().fromJson(json, CityQueryResponseItem::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return CityQueryResponseItem()
    }
}

fun File.readFile(): String {
    val inputStream: InputStream = this.inputStream()
    return inputStream.bufferedReader().use { it.readText() }
}