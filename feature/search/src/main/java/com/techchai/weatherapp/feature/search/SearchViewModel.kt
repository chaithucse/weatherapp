package com.techchai.weatherapp.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techchai.weatherapp.core.network.response.CityQueryResponseItem
import com.techchai.weatherapp.data.location.LocationHelper
import com.techchai.weatherapp.data.Result
import com.techchai.weatherapp.data.WeatherRepositoryImpl
import com.techchai.weatherapp.data.datastore.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @OptIn(ExperimentalCoroutinesApi::class)
@Inject constructor(
    private val repositoryImpl: WeatherRepositoryImpl,
    private val locationHelper: LocationHelper,
    private val appDataStore: AppDataStore
) : ViewModel() {

    private var _uiState = MutableStateFlow(SearchUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    fun saveLatValue(latitude: Double) {
        viewModelScope.launch {
            appDataStore.saveLatitudeToPreferencesStore(latitude)
        }
    }

    fun saveLongValue(latitude: Double) {
        viewModelScope.launch {
            appDataStore.saveLongitudeToPreferencesStore(latitude)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchCityList(query: String) {
        viewModelScope.launch {
            _uiState.value.isLoading = true
            locationHelper.getCurrentLocation()?.let { location ->
                val response = repositoryImpl.searchWeather(
                    query
                )
                when (response) {
                    is Result.Success -> {
                        _uiState.update {
                            _uiState.value.copy(
                                isLoading = false,
                                queryCityList = response.data
                            )
                        }
                    }

                    is Result.Error -> {}
                    else -> {}
                }
            }
        }
    }
}

data class SearchUiState(
    var isLoading: Boolean = false,
    val queryCityList: List<CityQueryResponseItem> = emptyList()
)