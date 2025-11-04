package com.grupo8.appclima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grupo8.appclima.ui.theme.AppClimaGrupo8Theme
import com.grupo8.appclima.ui.theme.presentaciones.ciudades.CiudadesPage
import com.grupo8.appclima.ui.theme.presentaciones.clima.ClimaPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppClimaGrupo8Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "ciudades",
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable(route = "ciudades"){
                            CiudadesPage(navController = navController)
                        }

                        composable(
                            route = "clima/{lat}/{lon}/{ciudad}",
                            arguments =  listOf(
                                navArgument("lat") { type= NavType.FloatType },
                                navArgument("lon") { type= NavType.FloatType },
                                navArgument("ciudad") { type= NavType.StringType }
                            )
                        ){
                            val ciudad = it.arguments?.getString("ciudad") ?: ""
                            val lat = it.arguments?.getFloat("lat") ?: 0.0f
                            val lon = it.arguments?.getFloat("lon") ?: 0.0f

                            ClimaPage(navController, lat = lat, lon = lon, ciudad = ciudad)
                        }
                    }
                }
            }
        }
    }
}