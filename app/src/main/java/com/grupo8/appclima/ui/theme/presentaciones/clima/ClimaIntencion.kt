package com.grupo8.appclima.ui.theme.presentaciones.clima

sealed class ClimaIntencion {
    data object Volver : ClimaIntencion()
    data class Compartir(val texto: String) : ClimaIntencion()
}