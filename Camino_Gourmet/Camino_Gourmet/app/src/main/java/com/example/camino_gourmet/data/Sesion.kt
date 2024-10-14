package com.example.camino_gourmet.data

class Sesion {
    companion object {
        var restaurantMode = ""
        var userName = ""
        var nombre = ""
        var apellido = ""
        var email = ""
        var esRestaurante = false
        var restaurante: Map<String, Any> = mapOf(
            "nombre" to "",
            "categoria" to "",
            "calificacion" to 0.0,
            "longitud" to 0.0,
            "latitud" to 0.0
        )
    }
}