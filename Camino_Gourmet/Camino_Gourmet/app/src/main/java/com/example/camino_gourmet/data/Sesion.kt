package com.example.camino_gourmet.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class Sesion {
    companion object {
        var restaurantMode = ""
        var userName = ""
        var nombre = ""
        var apellido = ""
        var email = ""
        var esRestaurante = false
        var restaurante: MutableMap<String, Any> = mutableMapOf(
            "nombre" to "",
            "categoria" to "",
            "calificacion" to 0.0,
            "longitud" to 0.0,
            "latitud" to 0.0
        )
        var auth = Firebase.auth
    }
}