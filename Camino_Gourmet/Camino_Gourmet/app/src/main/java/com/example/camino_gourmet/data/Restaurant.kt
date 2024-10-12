package com.example.camino_gourmet.data

data class Restaurant(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val calificacion: Double,
    val longitud: Double,
    val latitud: Double
)

