package com.techchai.weatherapp.data

import com.techchai.weatherapp.core.network.response.CityQueryResponseItem
import com.techchai.weatherapp.core.network.response.CurrentWeather
import com.techchai.weatherapp.core.network.response.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherInfo(lat: Double, long: Double, units: String) : Result<WeatherInfo>
    suspend fun searchWeather(cityName: String) : Result<List<CityQueryResponseItem>>
    suspend fun getCurrentWeather(lat: Double, long: Double, units: String) : Result<CurrentWeather>

}