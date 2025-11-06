package com.grupo8.appclima.ui.theme.presentaciones.clima

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo8.appclima.ui.theme.repositorio.modelos.Clima
import com.grupo8.appclima.ui.theme.repositorio.modelos.ListForecast
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ClimaView(
    estado: ClimaEstado,
    ciudad: String,
    onAction: (ClimaIntencion) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (estado) {
            is ClimaEstado.Cargando -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ClimaEstado.Error -> {
                Text(text = estado.mensaje, modifier = Modifier.align(Alignment.Center))
            }
            is ClimaEstado.Exitoso -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = ciudad, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Detalle de hoy
                    DetalleClimaHoy(clima = estado.datos.clima)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Gráfico (pronóstico)
                    PronosticoProximosDias(pronostico = estado.datos.pronostico)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones
                    Button(onClick = { /* TODO: Implementar Compartir */ }) {
                        Text(text = "Compartir")
                    }
                    Button(onClick = { onAction(ClimaIntencion.Volver) }) {
                        Text(text = "Cambiar de ciudad")
                    }
                }
            }
        }
    }
}

@Composable
fun DetalleClimaHoy(clima: Clima) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Hoy", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Text(text = "${clima.main.temp.toInt()}°C", fontSize = 48.sp, fontWeight = FontWeight.Bold)
            Text(text = clima.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "No disponible")
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Max: ${clima.main.temp_max.toInt()}°C")
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Min: ${clima.main.temp_min.toInt()}°C")
            }
        }
    }
}

@Composable
fun PronosticoProximosDias(pronostico: List<ListForecast>) {
    val pronosticoDiario = pronostico.distinctBy {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.dt * 1000))
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Próximos 5 días", fontSize = 20.sp, fontWeight = FontWeight.Medium)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pronosticoDiario) { forecast ->
                PronosticoItem(forecast)
            }
        }
    }
}

@Composable
fun PronosticoItem(forecast: ListForecast) {
    val date = Date(forecast.dt * 1000) // Convertir de segundos a milisegundos
    val diaFormatter = SimpleDateFormat("EEE", Locale.getDefault()) // "Mar"
    val dia = diaFormatter.format(date)

    Card {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = dia, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${forecast.main.temp.toInt()}°C")
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(text = "Max: ${forecast.main.temp_max.toInt()}°", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Min: ${forecast.main.temp_min.toInt()}°", fontSize = 12.sp)
            }
        }
    }
}