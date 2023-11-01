package com.techchai.weatherapp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val LOCATION_LATITUDE = doublePreferencesKey("latitude")
    private val LOCATION_LONGITUDE = doublePreferencesKey("longitude")

    suspend fun saveLatitudeToPreferencesStore(name: Double) {
        dataStore.edit { preferences ->
            preferences[LOCATION_LATITUDE] = name
        }
    }

    val latitudePreference: Flow<Double?> = dataStore.data
        .catch {
            // throws an IOException when an error is encountered when reading data
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[LOCATION_LATITUDE]

        }

    suspend fun saveLongitudeToPreferencesStore(name: Double) {
        dataStore.edit { preferences ->
            preferences[LOCATION_LONGITUDE] = name
        }
    }

    val longitudePreference: Flow<Double?> = dataStore.data
        .catch {
            // throws an IOException when an error is encountered when reading data
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[LOCATION_LONGITUDE]

        }
}