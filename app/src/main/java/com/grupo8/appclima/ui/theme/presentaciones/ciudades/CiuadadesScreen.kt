package com.grupo8.appclima.ui.theme.presentaciones.ciudades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CiudadesScreen(navController: NavController, ciudades: List<CiudadVm>, modifier: Modifier) {
    var ciudadSeleccionada by remember { mutableStateOf("") }

    val ciudadesFiltradas = if (ciudadSeleccionada.isBlank()) {
        ciudades
    } else {
        ciudades.filter { it.nombre.contains(ciudadSeleccionada, ignoreCase = true) }
    }

    Column(modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = modifier.align(Alignment.CenterHorizontally),
            text = "El Clima en tu Ciudad")
        FiltroCiudad(ciudad = ciudadSeleccionada, onCityChange = {
            ciudadSeleccionada = it
        })

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            items(ciudadesFiltradas) { ciudad ->
                CiudadCard(ciudad = ciudad) {
                    //Esta función se ejecutará con un click en la card
                    println("Ciudad seleccionada: ${ciudad.nombre}")
                    navController.navigate("clima?ciudadId=${ciudad.id}")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FiltroCiudad(ciudad: String, onCityChange: (String) -> Unit) {
    OutlinedTextField(
        value = ciudad,
        onValueChange = {
            onCityChange(it)
        },
        label = { Text("Buscar ciudad") }
    )
}
