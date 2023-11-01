package com.techchai.weatherapp.feature.weatherinfo.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.techchai.weatherapp.feature.weatherinfo.ui.WeatherScreen

fun NavGraphBuilder.weatherInfoNavGraph(navController: NavController) {
    composable("weather") {
        WeatherScreen() {
            navController.navigate("search")
        }
    }
}