package com.grupo8.appclima.ui.theme.presentaciones.clima

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ClimaScreen(navController: NavController, climaVm: ClimaVm, modifier: Modifier){
    Column() {
        Text(
            text = "${climaVm.nombre} esta ${climaVm.descripcion}"
        )
        Button(
            onClick = { navController.navigate("ciudades") }
        ) {
            Text(
                text = "Volver"
            )
        }
    }
}