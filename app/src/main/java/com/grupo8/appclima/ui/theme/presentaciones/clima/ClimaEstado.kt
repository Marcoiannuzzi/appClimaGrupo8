package com.grupo8.appclima.ui.theme.presentaciones.clima

import com.grupo8.appclima.ui.theme.repositorio.modelos.Clima
import com.grupo8.appclima.ui.theme.repositorio.modelos.ListForecast

data class PronosticoCompleto(
    val clima: Clima,
    val pronostico: List<ListForecast>
)

sealed class ClimaEstado {
    data object Cargando: ClimaEstado()
    data class Exitoso (val datos: PronosticoCompleto) : ClimaEstado()
    data class Error(val mensaje :String = "") : ClimaEstado()
}