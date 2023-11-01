package com.techchai.weatherapp.core.network.response

data class WeatherInfo(
	val current: Current,
	val timezone: String? = null,
	val daily: List<DailyItem>,
	val hourly: List<HourlyItem>
)

data class Temp(
	val min: Float? = null,
	val max: Float? = null
)

data class WeatherItem(
	val icon: String,
	val description: String,
	val main: String,
	val id: Int
)

data class HourlyItem(
	val temp: Float,
	val dt: Long,
	val weather: List<WeatherItem>
)

data class DailyItem(
	val temp: Temp,
	val dt: Long,
	val weather: List<WeatherItem>
)

data class Current(
	val temp: Float,
	val feelsLike: Float,
	val sunset: Int? = null,
	val weather: List<WeatherItem>
)

