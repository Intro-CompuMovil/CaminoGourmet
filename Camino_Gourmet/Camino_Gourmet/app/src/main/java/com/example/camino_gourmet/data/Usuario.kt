package com.example.camino_gourmet.data

data class Usuario(
    val id: Int,
    val userName: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val restaurante: Restaurante,
    val contrasena: String
)
