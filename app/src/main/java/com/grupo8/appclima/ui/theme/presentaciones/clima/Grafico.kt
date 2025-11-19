package com.grupo8.appclima.ui.theme.presentaciones.clima

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.grupo8.appclima.ui.theme.repositorio.modelos.ListForecast
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GraficoPronostico(pronostico: List<ListForecast>) {
    if (pronostico.isEmpty()) return

    val pronosticoAgrupadoPorDia = pronostico.groupBy {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.dt * 1000))
    }.values.toList().take(5)

    if (pronosticoAgrupadoPorDia.isEmpty()) return

    val minTempGeneral = pronosticoAgrupadoPorDia.minOf { dia -> dia.minOf { it.main.temp_min } }
    val maxTempGeneral = pronosticoAgrupadoPorDia.maxOf { dia -> dia.maxOf { it.main.temp_max } }
    val tempRange = (maxTempGeneral - minTempGeneral).coerceAtLeast(1.0)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 16.dp)
    ) {
        val ancho = size.width
        val alto = size.height
        val espacioEntrePuntos = ancho / (pronosticoAgrupadoPorDia.size - 1).coerceAtLeast(1)

        val pathMax = Path()
        val pathMin = Path()

        pronosticoAgrupadoPorDia.forEachIndexed { index, pronosticosDelDia ->
            val tempMaxDelDia = pronosticosDelDia.maxOf { it.main.temp_max }
            val tempMinDelDia = pronosticosDelDia.minOf { it.main.temp_min }

            val x = index * espacioEntrePuntos
            val yMax = alto - ((tempMaxDelDia - minTempGeneral) / tempRange * alto).toFloat()
            val yMin = alto - ((tempMinDelDia - minTempGeneral) / tempRange * alto).toFloat()

            if (index == 0) {
                pathMax.moveTo(x, yMax)
                pathMin.moveTo(x, yMin)
            } else {
                pathMax.lineTo(x, yMax)
                pathMin.lineTo(x, yMin)
            }

            drawCircle(Color.Red, radius = 8f, center = Offset(x, yMax))
            drawCircle(Color.Blue, radius = 8f, center = Offset(x, yMin))
        }

        drawPath(pathMax, color = Color.Red, style = Stroke(width = 5f))
        drawPath(pathMin, color = Color.Blue, style = Stroke(width = 5f))
    }
}