package com.grupo8.appclima.ui.theme.presentaciones.clima

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grupo8.appclima.ui.theme.repositorio.RepositorioApi

@Composable
fun ClimaPage(
    navController: NavHostController,
    lat: Float,
    lon: Float,
    ciudad: String
){
    val viewModel: ClimaViewModel = viewModel(
        factory = ClimaViewModelFactory(
            repositorio = RepositorioApi(),
            navController = navController,
            lat = lat,
            lon = lon,
            ciudad = ciudad
        )
    )

    ClimaView(estado = viewModel.uiState, ciudad = ciudad) { intencion, context ->
        viewModel.ejecutar(intencion, context)
    }
}