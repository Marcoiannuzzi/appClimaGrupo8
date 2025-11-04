package com.grupo8.appclima.ui.theme.presentaciones.ciudades

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.grupo8.appclima.ui.theme.repositorio.Repositorio
import com.grupo8.appclima.ui.theme.repositorio.modelos.Ciudad
import kotlinx.coroutines.launch

class CiudadesViewModel(
    val repositorio: Repositorio,
    val navHostController: NavHostController
) : ViewModel(){

    var uiState by mutableStateOf<CiudadesEstado>(CiudadesEstado.Vacio)
    var ciudades : List<Ciudad> = emptyList()

    fun ejecutar(intencion: CiudadesIntencion){
        when(intencion){
            is CiudadesIntencion.Buscar -> buscar(nombre = intencion.nombre)
            is CiudadesIntencion.Seleccionar -> seleccionar(ciudad = intencion.ciudad)
        }
    }

    private fun buscar( nombre: String){
        if (nombre.isBlank()) {
            uiState = CiudadesEstado.Vacio
            return
        }

        uiState = CiudadesEstado.Cargando
        viewModelScope.launch {
            try {
                ciudades = repositorio.buscarCiudad(nombre)
                if (ciudades.isEmpty()) {
                    uiState = CiudadesEstado.Vacio
                } else {
                    uiState = CiudadesEstado.Exitoso(ciudades)
                }
            } catch (exception: Exception){
                exception.printStackTrace()
                uiState = CiudadesEstado.Error(exception.message ?: "error desconocido")
            }
        }
    }

    private fun seleccionar(ciudad: Ciudad){
        navHostController.navigate(
            route = "clima/${ciudad.lat.toFloat()}/${ciudad.lon.toFloat()}/${ciudad.name}"
        )
    }

}


@Suppress("UNCHECKED_CAST")
class CiudadesViewModelFactory(
    private val repositorio: Repositorio,
    private val navHostController: NavHostController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CiudadesViewModel::class.java)) {
            return CiudadesViewModel(repositorio, navHostController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
    }
