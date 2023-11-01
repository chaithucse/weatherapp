package com.techchai.weatherapp.feature.weatherinfo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.techchai.weatherapp.core.network.response.DailyItem
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun DailyForecastCard(daily: List<DailyItem>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp), shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF00A3E1),
            contentColor = Color.White
        )
    ) {
        LazyColumn {
            items(daily) {
                DailyForeCastItems(it)
            }
        }
    }
}

@Composable
fun DailyForeCastItems(daily: DailyItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = getDate(daily.dt, "EEEE"), modifier = Modifier,
            style = MaterialTheme.typography.bodyLarge
        )
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${daily.weather.first().icon}@2x.png",
            contentDescription = "contentDescription",
            modifier = Modifier,
        )
        Text(
            text = "min: ${daily.temp.min?.toInt().toString()} °C  max: ${
                daily.temp.max?.toInt().toString()
            } °C", modifier = Modifier,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

fun getDate(utcInMillis: Long, formatPattern: String): String {
    val sdf = SimpleDateFormat(formatPattern)
    val dateFormat = Date(utcInMillis * 1000)
    return sdf.format(dateFormat)
}