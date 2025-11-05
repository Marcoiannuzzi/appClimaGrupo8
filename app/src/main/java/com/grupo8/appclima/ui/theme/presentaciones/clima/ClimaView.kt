package com.grupo8.appclima.ui.theme.presentaciones.clima

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo8.appclima.ui.theme.repositorio.modelos.Clima
import com.grupo8.appclima.ui.theme.repositorio.modelos.ListForecast
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth

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

                    // Botones de acción
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
    }.take(5)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Próximos 5 días",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        GraficoPronostico(pronosticoDiario)
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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

@Composable
fun GraficoPronostico(pronostico: List<ListForecast>) {
    // Tomamos solo 5 días
    val dias = pronostico.take(5)
    if (dias.isEmpty()) return

    val maxTemp = dias.maxOf { it.main.temp_max }
    val minTemp = dias.minOf { it.main.temp_min }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color(0xFFEAF2F8))
    ) {
        val ancho = size.width
        val alto = size.height
        val espacio = ancho / (dias.size - 1)

        val pathMax = Path()
        val pathMin = Path()

        dias.forEachIndexed { index, dia ->
            val x = index * espacio
            val yMax = alto - ((dia.main.temp_max - minTemp) / (maxTemp - minTemp) * alto).toFloat()
            val yMin = alto - ((dia.main.temp_min - minTemp) / (maxTemp - minTemp) * alto).toFloat()

            if (index == 0) {
                pathMax.moveTo(x, yMax)
                pathMin.moveTo(x, yMin)
            } else {
                pathMax.lineTo(x, yMax)
                pathMin.lineTo(x, yMin)
            }
        }

        // Línea de temperatura máxima
        drawPath(pathMax, color = Color.Red, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))

        // Línea de temperatura mínima
        drawPath(pathMin, color = Color.Blue, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
    }
}