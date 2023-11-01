package com.techchai.weatherapp.feature.search

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchScreen(onSelectCity: () -> Unit) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var query: String by rememberSaveable { mutableStateOf("") }

    Scaffold(topBar = {
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(2.dp, Color.DarkGray, RoundedCornerShape(30.dp)),
            value = query,
            onValueChange = {
                query = it
                if (it.isNotEmpty()) {
                    viewModel.fetchCityList(it)
                }
            },
            placeholder = {
                Text(
                    text = "Type city name",
                    color = Color.Black
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            ),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        query = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.Black
                    )
                }
            })
    }) {
        Box(modifier = Modifier.padding(it)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.queryCityList) {
                    CityFilterCard(it.name, it.country) {
                        it.lat?.let { it1 -> viewModel.saveLatValue(it1) }
                        it.lon?.let { it1 -> viewModel.saveLongValue(it1) }
                        onSelectCity()
                    }
                }
            }
        }
    }
}

@Composable
fun CityFilterCard(cityName: String?, country: String?, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 25.dp)
        .clickable {
            onClick()
        }) {
        Text(
            text = cityName ?: "",
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = country ?: "",
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

