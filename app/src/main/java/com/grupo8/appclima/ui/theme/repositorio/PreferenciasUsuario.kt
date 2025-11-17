package com.grupo8.appclima.ui.theme.repositorio

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class CiudadGuardada(
    val nombre: String,
    val lat: Float,
    val lon: Float
)

class PreferenciasUsuario(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("ultima_ciudad")
        val CIUDAD_NOMBRE = stringPreferencesKey("ultima_ciudad_nombre")
        val CIUDAD_LAT = floatPreferencesKey("ultima_ciudad_lat")
        val CIUDAD_LON = floatPreferencesKey("ultima_ciudad_lon")
    }

    // Función para guardar la ciudad
    suspend fun guardarUltimaCiudad(ciudad: CiudadGuardada) {
        context.dataStore.edit { preferences ->
            preferences[CIUDAD_NOMBRE] = ciudad.nombre
            preferences[CIUDAD_LAT] = ciudad.lat
            preferences[CIUDAD_LON] = ciudad.lon
        }
    }

    // Flow para leer la ciudad guardada
    val leerUltimaCiudad: Flow<CiudadGuardada?> = context.dataStore.data.map { preferences ->
        val nombre = preferences[CIUDAD_NOMBRE]
        val lat = preferences[CIUDAD_LAT]
        val lon = preferences[CIUDAD_LON]

        if (nombre != null && lat != null && lon != null) {
            CiudadGuardada(nombre, lat, lon)
        } else {
            null
        }
    }
}