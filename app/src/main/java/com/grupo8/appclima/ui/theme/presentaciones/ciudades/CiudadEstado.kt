package com.grupo8.appclima.ui.theme.presentaciones.ciudades

import com.grupo8.appclima.ui.theme.repositorio.modelos.Ciudad

sealed class CiudadesEstado {
    data object Vacio: CiudadesEstado()
    data object Cargando: CiudadesEstado()
    data class Exitoso( val ciudades : List<Ciudad> ) : CiudadesEstado()
    data class Error(val mensaje: String): CiudadesEstado()
}