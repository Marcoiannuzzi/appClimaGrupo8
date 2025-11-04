package com.grupo8.appclima.ui.theme.presentaciones.ciudades

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // <-- AÑADIR ESTE IMPORT
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth // <-- CAMBIAR DE fillMaxSize a fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grupo8.appclima.ui.theme.Purple40
import com.grupo8.appclima.ui.theme.Purple80
import com.grupo8.appclima.ui.theme.repositorio.modelos.Ciudad

@Composable
fun CiudadCard(ciudad: Ciudad, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .background(color = Purple40, shape = RoundedCornerShape(10.dp))
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = ciudad.name, color = Purple80)
        Text(text = ciudad.country, color = Purple80)
    }
}