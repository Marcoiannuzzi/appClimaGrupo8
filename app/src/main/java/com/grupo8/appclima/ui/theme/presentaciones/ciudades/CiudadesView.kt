package com.grupo8.appclima.ui.theme.presentaciones.ciudades
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.grupo8.appclima.ui.theme.repositorio.modelos.Ciudad

@Composable
fun CiudadesView(
    estado: CiudadesEstado,
    onAction: (CiudadesIntencion) -> Unit
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        FiltroCiudad( onCiudadChange = { it ->
            onAction(CiudadesIntencion.Buscar(it))
        },
            onBuscarPorGeolocalizacion = { lat, lon ->
                onAction(CiudadesIntencion.BuscarPorCoordenadas(lat, lon))
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
    onCiudadChange: (String) -> Unit,
    onBuscarPorGeolocalizacion: (Double, Double) -> Unit
) {
    var texto by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Solicitar permisos
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Permiso concedido, obtener ubicación
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Usar getCurrentLocation para una ubicación más fresca
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            onBuscarPorGeolocalizacion(location.latitude, location.longitude)
                        } else {
                            // Manejar el caso de ubicación nula (ej. servicios desactivados)
                        }
                    }
            }
        } else {
            // Permiso denegado
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = "El Clima en tu Ciudad"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = texto,
                onValueChange = {
                    texto = it
                    onCiudadChange(it)
                },
                label = { Text("Buscar ciudad") },
                modifier = Modifier.weight(1f) // El campo de texto ocupa el espacio restante
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Botón de geolocalización
            IconButton(onClick = {
                // Verificar permisos
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Permisos ya concedidos, obtener ubicación
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                        .addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                onBuscarPorGeolocalizacion(location.latitude, location.longitude)
                            }
                        }
                } else {
                    // Solicitar permisos
                    locationPermissionLauncher.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                }
            }) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Buscar por geolocalización"
                )
            }
        }
    }
}