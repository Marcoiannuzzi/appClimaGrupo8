package com.grupo8.appclima.ui.theme.presentaciones.clima

import com.grupo8.appclima.ui.theme.repositorio.modelos.Clima

sealed class ClimaEstado {
    data object Vacio: ClimaEstado()
    data object Cargando: ClimaEstado()
    data class Exitoso (val clima: Clima) : ClimaEstado()
    data class Error(val mensaje :String = "") : ClimaEstado()
}