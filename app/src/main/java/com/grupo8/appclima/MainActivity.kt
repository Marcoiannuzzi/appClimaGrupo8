package com.grupo8.appclima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grupo8.appclima.ui.theme.AppClimaGrupo8Theme
import com.grupo8.appclima.ui.theme.presentaciones.ciudades.CiudadesScreen
import com.grupo8.appclima.ui.theme.presentaciones.ciudades.ciudades
import com.grupo8.appclima.ui.theme.presentaciones.clima.ClimaScreen
import com.grupo8.appclima.ui.theme.presentaciones.clima.climaVm

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
                            CiudadesScreen(navController, ciudades, modifier = Modifier)
                        }
                        composable(route = "clima?ciudadId={ciudadId}"){
                            val ciudad = it.arguments?.getString("ciudadId") ?: ""

                            ClimaScreen(navController, climaVm = climaVm, modifier = Modifier)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppClimaGrupo8Theme {
        Greeting("Android")
    }
}