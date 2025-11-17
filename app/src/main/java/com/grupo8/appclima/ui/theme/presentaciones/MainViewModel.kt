package com.grupo8.appclima.ui.theme.presentaciones

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.grupo8.appclima.ui.theme.repositorio.CiudadGuardada
import com.grupo8.appclima.ui.theme.repositorio.Repositorio
import com.grupo8.appclima.ui.theme.repositorio.RepositorioApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class StartState {
    data object Loading: StartState()
    data object IrACiudades: StartState()
    data class IrAClima(val ciudad: CiudadGuardada): StartState()
}

class MainViewModel(private val repositorio: Repositorio) : ViewModel() {
    var startState by mutableStateOf<StartState>(StartState.Loading)
        private set

    init {
        viewModelScope.launch {
            val ciudadGuardada = repositorio.obtenerUltimaCiudad().first()
            startState = if (ciudadGuardada != null) {
                StartState.IrAClima(ciudadGuardada)
            } else {
                StartState.IrACiudades
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(RepositorioApi(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}