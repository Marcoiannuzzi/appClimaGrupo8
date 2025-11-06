package com.grupo8.appclima.ui.theme.repositorio

import com.grupo8.appclima.ui.theme.repositorio.modelos.Ciudad
import com.grupo8.appclima.ui.theme.repositorio.modelos.Clima
import com.grupo8.appclima.ui.theme.repositorio.modelos.ListForecast

interface Repositorio {
    suspend fun buscarCiudad(ciudad: String): List<Ciudad>
    suspend fun traerClima(lat: Float, lon: Float) : Clima
    suspend fun traerPronostico(nombre: String) : List<ListForecast>
    suspend fun buscarCiudadPorCoordenadas(lat: Double, lon: Double): List<Ciudad>
}