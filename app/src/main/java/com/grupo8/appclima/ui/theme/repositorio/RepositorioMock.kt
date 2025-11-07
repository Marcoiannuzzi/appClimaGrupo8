package com.grupo8.appclima.ui.theme.repositorio

import com.grupo8.appclima.ui.theme.repositorio.modelos.Ciudad
import com.grupo8.appclima.ui.theme.repositorio.modelos.Clima
import com.grupo8.appclima.ui.theme.repositorio.modelos.ListForecast

class RepositorioMock  : Repositorio {

    val cordoba = Ciudad(name = "Cordoba",
        lat = -23.0,
        lon = -24.3,
        country = "Argentina")
    val bsAs = Ciudad(name = "Buenos Aires",
        lat = -23.0,
        lon = -24.3,
        country = "Argentina")
    val laPlata = Ciudad(
        name = "La Plata",
        lat = -23.0,
        lon = -24.3,
        country = "Argentina"
    )

    val ciudades = listOf(cordoba,bsAs,laPlata)

    override suspend fun buscarCiudad(ciudad: String): List<Ciudad> {
        if (ciudad == "error"){
            throw Exception()
        }
        return ciudades.filter { it.name.contains(ciudad,ignoreCase = true) }
    }

    override suspend fun buscarCiudadPorCoordenadas(lat: Double, lon: Double): List<Ciudad> {
        return listOf(bsAs)
    }

    override suspend fun traerClima(lat: Float, lon: Float): Clima {
        TODO("Not yet implemented")
    }

    override suspend fun traerPronostico(nombre: String): List<ListForecast> {
        TODO("Not yet implemented")
    }
}


class RepositorioMockError  : Repositorio {

    override suspend fun buscarCiudad(ciudad: String): List<Ciudad> {
        throw Exception()
    }

    // Implementación Mock añadida
    override suspend fun buscarCiudadPorCoordenadas(lat: Double, lon: Double): List<Ciudad> {
        throw Exception()
    }

    override suspend fun traerClima(lat: Float, lon: Float): Clima {
        throw Exception()
    }

    override suspend fun traerPronostico(nombre: String): List<ListForecast> {
        throw Exception()
    }
}