package com.grupo8.appclima.ui.theme.presentaciones.clima

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

import com.grupo8.appclima.ui.theme.repositorio.modelos.ListForecast

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
            .height(180.dp)
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

            // Puntos visibles
            drawCircle(androidx.compose.ui.graphics.Color.Red, radius = 6f, center = Offset(x, yMax))
            drawCircle(androidx.compose.ui.graphics.Color.Blue, radius = 6f, center = Offset(x, yMin))
        }

        drawPath(pathMax, color = androidx.compose.ui.graphics.Color.Red, style = Stroke(width = 4f))
        drawPath(pathMin, color = androidx.compose.ui.graphics.Color.Blue, style = Stroke(width = 4f))
    }
}