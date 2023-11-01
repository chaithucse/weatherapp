package com.techchai.weatherapp.feature.weatherinfo

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.techchai.weatherapp.core.network.response.Current
import com.techchai.weatherapp.core.network.response.DailyItem
import com.techchai.weatherapp.core.network.response.HourlyItem
import com.techchai.weatherapp.core.network.response.WeatherInfo
import com.techchai.weatherapp.core.network.response.WeatherItem
import com.techchai.weatherapp.data.Result
import com.techchai.weatherapp.data.WeatherRepositoryImpl
import com.techchai.weatherapp.data.datastore.AppDataStore
import com.techchai.weatherapp.data.location.LocationHelper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.io.InputStream
import kotlin.test.assertFalse

class WeatherViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repositoryImpl = mockk<WeatherRepositoryImpl>(relaxed = true)
    private val locationHelper = mockk<LocationHelper>(relaxed = true)
    private val appDataStore = mockk<AppDataStore>(relaxed = true)

    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        viewModel = WeatherViewModel(repositoryImpl, locationHelper, appDataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFetchOneCall() = runTest {
        coEvery { repositoryImpl.getWeatherInfo(1.0, 1.0, "") }.returns(
            Result.Success(getWeatherInfo("weather.json"))
        )
        val weatherData = viewModel.uiState.value.weatherInfo
        assertFalse(weatherData?.daily?.isEmpty() == true)
    }

    private fun getWeatherInfo(filename: String): WeatherInfo {
        try {
            val json = File("$filename").readFile()
            return Gson().fromJson(json, WeatherInfo::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val dailyList = emptyList<DailyItem>()
        val hourlyList = emptyList<HourlyItem>()
        val weatherItem = emptyList<WeatherItem>()
        val current = Current(1.0f, 1.0f, weather = weatherItem)
        return WeatherInfo(current = current, hourly = hourlyList, daily = dailyList)
    }
}

fun File.readFile(): String {
    val inputStream: InputStream = this.inputStream()
    return inputStream.bufferedReader().use { it.readText() }
}