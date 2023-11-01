package com.techchai.weatherapp.feature.weatherinfo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.techchai.weatherapp.core.network.response.HourlyItem

@Composable
fun HoursForecastCard(lists: List<HourlyItem>) {
    Card(
        modifier = Modifier.padding(all = 12.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF00A3E1),
            contentColor = Color.White
        )
    ) {
        LazyRow {
            items(items = lists) {
                HourlyRowItem(it)
            }
        }
    }
}

@Composable
fun HourlyRowItem(hourItem: HourlyItem) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 4.dp)
    ) {
        ForecastedTime(text = getDate(hourItem.dt, "hh a"))
        WeatherIcon(
            iconUrl = hourItem.weather.first().icon,
            contentDescription = hourItem.weather.first().description,
            modifier = Modifier
                .padding(4.dp)
        )
        Temperature(text = hourItem.temp.toInt().toString() + " °C")
    }
}

@Composable
fun Headline(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun Body(text: String, modifier: Modifier = Modifier, color: Color = Color.Unspecified) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        modifier = modifier
    )
}

@Composable
fun Temperature(text: String) {
    Body(
        text = text,
        modifier = Modifier.padding(4.dp)
    )
}

@Composable
fun WeatherIcon(iconUrl: String, contentDescription: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = "https://openweathermap.org/img/wn/$iconUrl@2x.png",
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

@Composable
fun ForecastedTime(text: String, modifier: Modifier = Modifier) {
    Body(
        text = text,
        modifier = modifier.padding(4.dp)
    )
}