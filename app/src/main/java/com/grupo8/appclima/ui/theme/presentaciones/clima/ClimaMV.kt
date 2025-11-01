package com.grupo8.appclima.ui.theme.presentaciones.clima

data class ClimaVm(
    val id : String,
    val nombre: String,
    val descripcion: String
)


val climaVm = ClimaVm(
    id = "1",
    nombre = "Buenos Aires",
    descripcion = "Soleado"
)