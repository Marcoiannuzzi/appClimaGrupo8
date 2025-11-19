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
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage

@Composable
fun ClimaView(
    estado: ClimaEstado,
    ciudad: String,
    onAction: (ClimaIntencion, android.content.Context) -> Unit
) {
    val context = LocalContext.current

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
                    GraficoPronostico(pronostico = estado.datos.pronostico)
                    Spacer(modifier = Modifier.height(24.dp))
                    PronosticoProximosDias(pronostico = estado.datos.pronostico)

                    val clima = estado.datos.clima
                    // Botones de acción
                    Button(onClick = {
                        val texto = generarTextoCompartir(ciudad, clima)
                        onAction(ClimaIntencion.Compartir(texto), context)
                    }) {
                        Text(text = "Compartir")
                    }
                    Button(onClick = { onAction(ClimaIntencion.Volver, context) }) {
                        Text(text = "Cambiar de ciudad")
                    }
                }
            }
        }
    }
}

@Composable
fun DetalleClimaHoy(clima: Clima) {
    val iconCode = clima.weather.firstOrNull()?.icon

    Card(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna para todo el texto a la izquierda
            Column(modifier = Modifier.weight(1.5f)) {
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

            Spacer(modifier = Modifier.width(16.dp))

            if (iconCode != null) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${iconCode}@4x.png",
                    contentDescription = clima.weather.firstOrNull()?.description,
                    modifier = Modifier.weight(1f).size(120.dp)
                )
            }
        }
    }
}

@Composable
fun PronosticoProximosDias(pronostico: List<ListForecast>) {
    val pronosticoAgrupadoPorDia = pronostico.groupBy {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.dt * 1000))
    }.values.toList()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Próximos 5 días", fontSize = 20.sp, fontWeight = FontWeight.Medium)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pronosticoAgrupadoPorDia.take(5)) { pronosticosDelDia ->
                val tempMinCalculada = pronosticosDelDia.minOf { it.main.temp_min }.toInt()
                val tempMaxCalculada = pronosticosDelDia.maxOf { it.main.temp_max }.toInt()
                val forecastRepresentativo = pronosticosDelDia.first()

                PronosticoItem(
                    forecast = forecastRepresentativo,
                    tempMinCalculada = tempMinCalculada,
                    tempMaxCalculada = tempMaxCalculada
                )
            }
        }
    }
}

@Composable
fun PronosticoItem(
    forecast: ListForecast,
    tempMinCalculada: Int,
    tempMaxCalculada: Int
) {
    val date = Date(forecast.dt * 1000)
    val diaFormatter = SimpleDateFormat("EEE", Locale("es", "ES"))
    val dia = diaFormatter.format(date).replaceFirstChar { it.uppercase() }

    Card {
        Column(
            modifier = Modifier.padding(12.dp).width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = dia, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${forecast.main.temp.toInt()}°C", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${tempMaxCalculada}°/${tempMinCalculada}°", fontSize = 12.sp)
        }
    }
}


fun generarTextoCompartir(ciudad: String, clima: com.grupo8.appclima.ui.theme.repositorio.modelos.Clima): String {
    val descripcion = clima.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "No disponible"
    val temp = clima.main.temp.toInt()
    val max = clima.main.temp_max.toInt()
    val min = clima.main.temp_min.toInt()

    return """
        ☀️ Pronóstico del clima en $ciudad:
        Temperatura actual: $temp°C
        Máxima: $max°C / Mínima: $min°C
        Condición: $descripcion
        Compartido desde AppClima 🌦️
    """.trimIndent()
}



