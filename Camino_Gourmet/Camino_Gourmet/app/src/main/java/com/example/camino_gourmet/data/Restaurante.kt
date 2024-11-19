package com.example.camino_gourmet.data

data class Restaurante(
    val nombre: String,
    val categoria: String,
    val calificacion: Double,
    val longitud: Double,
    val latitud: Double,
    val visibilidad: Boolean
)
