package com.grupo8.appclima.ui.theme.presentaciones.ciudades

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.grupo8.appclima.ui.theme.repositorio.RepositorioApi

@Composable
fun CiudadesPage(
    navController: NavHostController,
){
    // <-- CONTEXTO AÑADIDO
    val context = LocalContext.current
    val viewModel = viewModel<CiudadesViewModel>(
        factory = CiudadesViewModelFactory(
            repositorio = RepositorioApi(context),
            navController)
    )

    CiudadesView(
        estado = viewModel.uiState
    ){ intencion ->
        viewModel.ejecutar(intencion)

    }
}