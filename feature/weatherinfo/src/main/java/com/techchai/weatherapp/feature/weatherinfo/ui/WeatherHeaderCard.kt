package com.techchai.weatherapp.feature.weatherinfo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techchai.weatherapp.feature.weatherinfo.WeatherUiState

@Composable
fun WeatherHeaderCard(uiState: State<WeatherUiState>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = uiState.value.currentWeather?.name ?: "",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = uiState.value.weatherInfo?.current?.temp?.toInt().toString() + "°C",
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = uiState.value.weatherInfo?.current?.weather?.first()?.description.toString(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium
        )
    }
}