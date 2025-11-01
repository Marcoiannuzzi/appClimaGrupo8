package com.grupo8.appclima.ui.theme.presentaciones.ciudades

data class CiudadVm(
    val id: String,
    val nombre: String,
    val descripcion: String
)

val ciudades: List<CiudadVm> = listOf(
    CiudadVm("1","Ciudad 1", "descripcion 1"),
    CiudadVm("2","Ciudad 2", "descripcion 2"),
    CiudadVm("3","Ciudad 3", "descripcion 3"),
    CiudadVm("4","Ciudad 4", "descripcion 4"),
    CiudadVm("5","Ciudad 5", "descripcion 5"),
    CiudadVm("6","Ciudad 6", "descripcion 6"),
    CiudadVm("7","Ciudad 7", "descripcion 7"),
    CiudadVm("8","Ciudad 8", "descripcion 8"),
    CiudadVm("9","Ciudad 9", "descripcion 9"),
    CiudadVm("10","Ciudad 10", "descripcion 10")

)