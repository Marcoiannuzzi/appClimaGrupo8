package com.grupo8.appclima.ui.theme.presentaciones.clima

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.grupo8.appclima.ui.theme.repositorio.Repositorio
import kotlinx.coroutines.launch
import android.content.Context

class ClimaViewModel(
    private val repositorio: Repositorio,
    private val navController: NavHostController,
    private val lat: Float,
    private val lon: Float,
    private val ciudad: String
) : ViewModel() {

    var uiState by mutableStateOf<ClimaEstado>(ClimaEstado.Cargando)
        private set

    init {
        obtenerClima()
    }

    private fun obtenerClima() {
        uiState = ClimaEstado.Cargando
        viewModelScope.launch {
            try {
                val climaActual = repositorio.traerClima(lat, lon)
                val pronostico = repositorio.traerPronostico(ciudad)

                // Una vez que ambos terminan, actualizamos el estado a Exitoso
                uiState = ClimaEstado.Exitoso(
                    datos = PronosticoCompleto(
                        clima = climaActual,
                        pronostico = pronostico
                    )
                )
            } catch (e: Exception) {
                uiState = ClimaEstado.Error("Error al obtener el pronóstico: ${e.message}")
            }
        }
    }

    fun ejecutar(intencion: ClimaIntencion, context: Context) {
        when (intencion) {
            ClimaIntencion.Volver -> volverASeleccionarCiudad()
            is ClimaIntencion.Compartir -> compartirPronostico(intencion.texto, context)

        }
    }

    private fun volverASeleccionarCiudad() {
        navController.navigate("ciudades")
    }
}

@Suppress("UNCHECKED_CAST")
class ClimaViewModelFactory(
    private val repositorio: Repositorio,
    private val navController: NavHostController,
    private val lat: Float,
    private val lon: Float,
    private val ciudad: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClimaViewModel::class.java)) {
            return ClimaViewModel(repositorio, navController, lat, lon, ciudad) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


private fun compartirPronostico(texto: String, context: android.content.Context) {
    val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(android.content.Intent.EXTRA_TEXT, texto)
    }
    context.startActivity(android.content.Intent.createChooser(intent, "Compartir pronóstico"))
}