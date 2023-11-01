package com.techchai.weatherapp.data

import com.techchai.weatherapp.core.network.api.ApiService
import com.techchai.weatherapp.core.network.response.CityQueryResponse
import com.techchai.weatherapp.core.network.response.CityQueryResponseItem
import com.techchai.weatherapp.core.network.response.CurrentWeather
import com.techchai.weatherapp.core.network.response.WeatherInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    WeatherRepository {

    override suspend fun getWeatherInfo(
        lat: Double,
        long: Double,
        units: String
    ): Result<WeatherInfo> {
        return withContext(Dispatchers.IO) {
            val onecall = apiService.getWeatherInfo(lat, long, units = "metric")
            if (onecall == null) {
                Result.Error(IllegalArgumentException("Post not found"))
            } else {
                Result.Success(onecall)
            }
        }
    }

    override suspend fun searchWeather(
        cityName: String
    ): Result<List<CityQueryResponseItem>> {
        return withContext(Dispatchers.IO) {
            val currentWeather = apiService.searchWeather(cityName, 5)
            if (currentWeather == null) {
                Result.Error(IllegalArgumentException("Post not found"))
            } else {
                Result.Success(currentWeather)
            }
        }
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        long: Double,
        units: String
    ): Result<CurrentWeather> {
        return withContext(Dispatchers.IO) {
            val currentWeather = apiService.getCurrentWeather(lat, long, units = "metric")
            if (currentWeather == null) {
                Result.Error(IllegalArgumentException("Post not found"))
            } else {
                Result.Success(currentWeather)
            }
        }
    }
}
