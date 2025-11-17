package com.grupo8.appclima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grupo8.appclima.ui.theme.AppClimaGrupo8Theme
import com.grupo8.appclima.ui.theme.presentaciones.MainViewModel
import com.grupo8.appclima.ui.theme.presentaciones.MainViewModelFactory
import com.grupo8.appclima.ui.theme.presentaciones.StartState
import com.grupo8.appclima.ui.theme.presentaciones.ciudades.CiudadesPage
import com.grupo8.appclima.ui.theme.presentaciones.clima.ClimaPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppClimaGrupo8Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val mainViewModel: MainViewModel = viewModel(
                        factory = MainViewModelFactory(LocalContext.current)
                    )

                    when (val state = mainViewModel.startState) {
                        is StartState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is StartState.IrACiudades -> {
                            AppNavigation(startDestination = "ciudades")
                        }
                        is StartState.IrAClima -> {
                            val ciudad = state.ciudad
                            val route = "clima/${ciudad.lat}/${ciudad.lon}/${ciudad.nombre}"
                            AppNavigation(startDestination = route)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = "ciudades") {
            CiudadesPage(navController = navController)
        }

        composable(
            route = "clima/{lat}/{lon}/{ciudad}",
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType },
                navArgument("ciudad") { type = NavType.StringType }
            )
        ) {
            val ciudad = it.arguments?.getString("ciudad") ?: ""
            val lat = it.arguments?.getFloat("lat") ?: 0.0f
            val lon = it.arguments?.getFloat("lon") ?: 0.0f

            ClimaPage(navController, lat = lat, lon = lon, ciudad = ciudad)
        }
    }
}