package com.techchai.weatherapp.navgraph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.techchai.weatherapp.feature.search.navgraph.searchNavGraph
import com.techchai.weatherapp.feature.weatherinfo.navgraph.weatherInfoNavGraph

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "weather", modifier = Modifier.fillMaxSize()) {
        weatherInfoNavGraph(navController)
        searchNavGraph(navController)
    }
}