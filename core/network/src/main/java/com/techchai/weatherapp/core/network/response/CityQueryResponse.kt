package com.techchai.weatherapp.core.network.response

data class CityQueryResponse(
	val cityQueryResponse: List<CityQueryResponseItem> = emptyList()
)

data class CityQueryResponseItem(
	val country: String? = null,
	val name: String? = null,
	val state: String? = null,
	val lon: Double? = null,
	val lat: Double? = null,
)