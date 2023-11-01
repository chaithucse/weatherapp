package com.techchai.weatherapp.feature.weatherinfo.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.techchai.weatherapp.feature.weatherinfo.R
import com.techchai.weatherapp.feature.weatherinfo.WeatherViewModel
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(onNavigateToSearch: () -> Unit) {
    val viewModel: WeatherViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            viewModel.fetchOneCall()
        }
    }

    getUserLocation(viewModel, context, permissions, launcherMultiplePermissions)

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.fetchOneCall()
            viewModel.fetchCurrentWeather()
        }
    }

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {

            if (uiState.value.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = Color.Blue
                )
            } else {
                BackgroundImage()
                Column {
                    MenuIcon { onNavigateToSearch() }
                    WeatherHeaderCard(uiState)
                    HoursForecastCard(uiState.value.weatherInfo?.hourly ?: emptyList())
                    DailyForecastCard(uiState.value.weatherInfo?.daily ?: emptyList())
                }
            }
        }
    }
}

@Composable
fun BackgroundImage() {
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun MenuIcon(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 24.dp, end = 24.dp)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.TopEnd
    ) {
        Image(imageVector = Icons.Default.Search, contentDescription = "")
    }
}

@Composable
fun isLocationEnabled(): Boolean {
    val locationManager =
        LocalContext.current.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}

//A callback for receiving notifications from the FusedLocationProviderClient.
lateinit var locationCallback: LocationCallback

//The main entry point for interacting with the Fused Location Provider
lateinit var locationProvider: FusedLocationProviderClient

@SuppressLint("MissingPermission")
@Composable
fun getUserLocation(
    viewModel: WeatherViewModel,
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
) {

    locationProvider = LocationServices.getFusedLocationProviderClient(context)

    var currentUserLocation by remember { mutableStateOf(LatandLong()) }

    DisposableEffect(key1 = locationProvider) {
        locationCallback = object : LocationCallback() {

            override fun onLocationResult(result: LocationResult) {

                for (location in result.locations) {
                    // Update data class with location data
                    currentUserLocation = LatandLong(location.latitude, location.longitude)
                    viewModel.setLatLongValue(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                }

                locationProvider.lastLocation
                    .addOnSuccessListener { location ->
                        location?.let {
                            val lat = location.latitude
                            val long = location.longitude
                            // Update data class with location data
                            currentUserLocation = LatandLong(latitude = lat, longitude = long)
                            viewModel.setLatLongValue(latitude = lat, longitude = long)

                        }
                    }
                    .addOnFailureListener {
                        Log.e("Location_error", "${it.message}")
                    }
            }
        }
        if (checkPermissions(context)) {
            locationUpdate()
        } else {
            launcher.launch(permissions)
        }
        onDispose {
        }
    }
}

//data class to store the user Latitude and longitude
data class LatandLong(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

private fun checkPermissions(context: Context): Boolean {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    }
    return false
}

@SuppressLint("MissingPermission")
fun locationUpdate() {
    locationCallback.let {
        //An encapsulation of various parameters for requesting
        // location through FusedLocationProviderClient.
        val locationRequest: LocationRequest =
            LocationRequest.create().apply {
                interval = TimeUnit.SECONDS.toMillis(60)
                fastestInterval = TimeUnit.SECONDS.toMillis(30)
                maxWaitTime = TimeUnit.MINUTES.toMillis(2)
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        //use FusedLocationProviderClient to request location update
        locationProvider.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}