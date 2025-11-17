package com.grupo8.appclima.ui.theme.repositorio

import android.content.Context
import com.grupo8.appclima.ui.theme.repositorio.modelos.Ciudad
import com.grupo8.appclima.ui.theme.repositorio.modelos.Clima
import com.grupo8.appclima.ui.theme.repositorio.modelos.ForecastDTO
import com.grupo8.appclima.ui.theme.repositorio.modelos.ListForecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

class RepositorioApi(context: Context) : Repositorio {

    private val preferencias = PreferenciasUsuario(context)
    private val apiKey = "58ddb70760aba34094cdeff2cb86f706"

    private val cliente = HttpClient(){
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun buscarCiudad(ciudad: String): List<Ciudad> {
        val respuesta = cliente.get("https://api.openweathermap.org/geo/1.0/direct"){
            parameter("q",ciudad)
            parameter("limit",10)
            parameter("appid",apiKey)
        }

        if (respuesta.status == HttpStatusCode.OK){
            val ciudades = respuesta.body<List<Ciudad>>()
            return ciudades
        }else{
            throw Exception()
        }
    }

    override suspend fun buscarCiudadPorCoordenadas(lat: Double, lon: Double): List<Ciudad> {
        val respuesta = cliente.get("https://api.openweathermap.org/geo/1.0/reverse"){
            parameter("lat",lat)
            parameter("lon",lon)
            parameter("limit",5)
            parameter("appid",apiKey)
        }

        if (respuesta.status == HttpStatusCode.OK){
            val ciudades = respuesta.body<List<Ciudad>>()
            return ciudades
        }else{
            throw Exception()
        }
    }

    override suspend fun traerClima(lat: Float, lon: Float): Clima {
        val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/weather"){
            parameter("lat",lat)
            parameter("lon",lon)
            parameter("units","metric")
            parameter("appid",apiKey)
        }
        if (respuesta.status == HttpStatusCode.OK){
            val clima = respuesta.body<Clima>()
            return clima
        }else{
            throw Exception()
        }
    }

    override suspend fun traerPronostico(nombre: String): List<ListForecast> {

        val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/forecast"){
            parameter("q",nombre)
            parameter("units","metric")
            parameter("appid",apiKey)
        }
        if (respuesta.status == HttpStatusCode.OK){
            val forecast = respuesta.body<ForecastDTO>()
            return forecast.list
        }else{
            throw Exception()
        }
    }

    override suspend fun guardarUltimaCiudad(ciudad: CiudadGuardada) {
        preferencias.guardarUltimaCiudad(ciudad)
    }

    override fun obtenerUltimaCiudad(): Flow<CiudadGuardada?> {
        return preferencias.leerUltimaCiudad
    }
}