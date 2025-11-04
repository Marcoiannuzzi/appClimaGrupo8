package com.grupo8.appclima.ui.theme.presentaciones.ciudades
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grupo8.appclima.ui.theme.repositorio.modelos.Ciudad

@Composable
fun CiudadesView(
    estado: CiudadesEstado,
    onAction: (CiudadesIntencion) -> Unit
    ) {

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        FiltroCiudad( onCiudadChange = { it ->
            onAction(CiudadesIntencion.Buscar(it))
        }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when(estado) {
            CiudadesEstado.Cargando -> Text(text = "cargando")
            is CiudadesEstado.Error -> Text(text = estado.mensaje)
            is CiudadesEstado.Exitoso -> {ListaDeCiudades(estado.ciudades){
                onAction(CiudadesIntencion.Seleccionar(it))
            }
            }
            CiudadesEstado.Vacio -> Text(text = "No hay resultados")
        }
    }
}

@Composable
fun ListaDeCiudades(ciudades: List<Ciudad>, onCiudadChange: (Ciudad) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {

        items(ciudades) { ciudad ->
            CiudadCard(ciudad = ciudad, onClick = {
                onCiudadChange(ciudad)
            })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FiltroCiudad(
    modifier: Modifier = Modifier,
    onCiudadChange: (String) -> Unit
    ) {
    var texto by rememberSaveable { mutableStateOf("") }
    Column {
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = "El Clima en tu Ciudad"
        )
        OutlinedTextField(
            value = texto,
            onValueChange = {
                texto = it
                onCiudadChange(it)
            },
            label = { Text("Buscar el clima en una ciudad") }
        )
    }
}