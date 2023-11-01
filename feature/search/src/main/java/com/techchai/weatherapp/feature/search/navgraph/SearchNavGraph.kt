package com.techchai.weatherapp.feature.search.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.techchai.weatherapp.feature.search.SearchScreen

fun NavGraphBuilder.searchNavGraph(navController: NavController) {
    composable("search") {
        SearchScreen {
            navController.popBackStack()
        }
    }
}