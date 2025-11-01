package com.grupo8.appclima.ui.theme.presentaciones.ciudades

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grupo8.appclima.ui.theme.Purple40
import com.grupo8.appclima.ui.theme.Purple80

@Composable
fun CiudadCard(ciudad: CiudadVm, onClickAction: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()
        .background(color = Purple40, shape = RoundedCornerShape(10.dp))
        .clickable { onClickAction() }
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = ciudad.nombre, color = Purple80)
        Text(text = ciudad.descripcion, color = Purple80)

    }
}