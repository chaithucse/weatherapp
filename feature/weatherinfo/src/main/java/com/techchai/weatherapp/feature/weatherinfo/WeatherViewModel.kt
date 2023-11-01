package com.techchai.weatherapp.feature.weatherinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techchai.weatherapp.core.network.response.CurrentWeather
import com.techchai.weatherapp.core.network.response.WeatherInfo
import com.techchai.weatherapp.data.location.LocationHelper
import com.techchai.weatherapp.data.Result
import com.techchai.weatherapp.data.WeatherRepositoryImpl
import com.techchai.weatherapp.data.datastore.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepositoryImpl,
    private val locationHelper: LocationHelper,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private var _uiState = MutableStateFlow(WeatherUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        fetchOneCall()
        fetchCurrentWeather()
    }

    fun fetchOneCall() {
        viewModelScope.launch {
            _uiState.value.isLoading = true
            val latitude = appDataStore.latitudePreference.first()
            val longitude = appDataStore.longitudePreference.first()
            locationHelper.getCurrentLocation()?.let { location ->
                val response = repository.getWeatherInfo(
                    lat = latitude ?: location.latitude,
                    long = longitude ?: location.longitude,
                    units = "metric"
                )
                when (response) {
                    is Result.Success -> {
                        _uiState.update {
                            _uiState.value.copy(
                                isLoading = false,
                                weatherInfo = response.data
                            )
                        }
                    }

                    is Result.Error -> {}
                    else -> {}
                }
            }
        }
    }

    fun fetchCurrentWeather() {
        viewModelScope.launch {
            _uiState.value.isLoading = true
            val latitude = appDataStore.latitudePreference.first()
            val longitude = appDataStore.longitudePreference.first()
            locationHelper.getCurrentLocation()?.let { location ->
                val response = repository.getCurrentWeather(
                    lat = latitude ?: location.latitude,
                    long = longitude ?: location.longitude,
                    units = "metric"
                )
                when (response) {
                    is Result.Success -> {
                        _uiState.update {
                            _uiState.value.copy(
                                isLoading = false,
                                currentWeather = response.data
                            )
                        }
                    }
                    is Result.Error -> {}
                    else -> {}
                }
            }
        }
    }

    fun setLatLongValue(latitude: Double?, longitude: Double?) {
        _uiState.update {
            _uiState.value.copy(latitude = latitude, longitude = longitude)
        }
    }
}

data class WeatherUiState(
    var isLoading: Boolean = false,
    val cityName: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val weatherInfo: WeatherInfo? = null,
    val currentWeather: CurrentWeather = CurrentWeather()
)