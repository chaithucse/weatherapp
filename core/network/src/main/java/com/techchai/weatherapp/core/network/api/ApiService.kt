package com.techchai.weatherapp.core.network.api

import com.techchai.weatherapp.core.network.response.CityQueryResponseItem
import com.techchai.weatherapp.core.network.response.CurrentWeather
import com.techchai.weatherapp.core.network.response.WeatherInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/3.0/onecall")
    suspend fun getWeatherInfo(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String
    ): WeatherInfo

    @GET("geo/1.0/direct")
    suspend fun searchWeather(
        @Query("q") query: String,
        @Query("limit") limit: Int
    ): List<CityQueryResponseItem>

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String
    ): CurrentWeather
}