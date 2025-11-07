package com.grupo8.appclima.ui.theme.presentaciones.ciudades

import com.grupo8.appclima.ui.theme.repositorio.modelos.Ciudad

sealed class CiudadesIntencion {
    data class Buscar( val nombre:String ) : CiudadesIntencion()

    data class Seleccionar(val ciudad: Ciudad) : CiudadesIntencion()

    data class BuscarPorCoordenadas(val lat: Double, val lon: Double) : CiudadesIntencion()
}