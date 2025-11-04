package com.grupo8.appclima.ui.theme.presentaciones.ciudades

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grupo8.appclima.ui.theme.repositorio.RepositorioApi

@Composable
fun CiudadesPage(
    navController: NavHostController,
){
    var viewModel = viewModel<CiudadesViewModel>(
        factory = CiudadesViewModelFactory(
            repositorio = RepositorioApi(),
            navController)
    )

    CiudadesView(
        estado = viewModel.uiState
    ){ intencion ->
        viewModel.ejecutar(intencion)

    }
}