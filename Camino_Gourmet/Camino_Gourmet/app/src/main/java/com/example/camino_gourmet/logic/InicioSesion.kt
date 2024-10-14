package com.example.camino_gourmet.logic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.camino_gourmet.R
import com.example.camino_gourmet.data.Usuario
import com.example.camino_gourmet.data.Funciones
import com.example.camino_gourmet.data.Sesion

class InicioSesion : AppCompatActivity() {

    lateinit var contrasena : EditText
    lateinit var nombre : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio_sesion)

        //Copiar archivo json desde assets a internal storage (si es necesario)
        Funciones.copyJsonToInternalStorageIfNeededUsers(this)
        Funciones.copyJsonToInternalStorageIfNeededComments(this)


        contrasena = findViewById<EditText>(R.id.Contraseña)
        nombre = findViewById<EditText>(R.id.Usuario)
        val TextView = findViewById<TextView>(R.id.CrearCuenta)
        val BotonIniciarSesion = findViewById<Button>(R.id.BotonIngreso)


        TextView.setOnClickListener {
            val intent = Intent(this, CreacionCuenta::class.java)
            startActivity(intent)
        }

        BotonIniciarSesion.setOnClickListener {
            botonIniciarSesion()
        }

    }

    fun botonIniciarSesion(){
        val nombre = nombre.text.toString()
        val contrasena = contrasena.text.toString()

        if (nombre.isNotEmpty() && contrasena.isNotEmpty()) {
            validarUsuario()
        } else{
            Toast.makeText(this, "Ingrese los campos para continuar", Toast.LENGTH_SHORT).show()
        }

    }

    fun validarUsuario(){
        //Obtener usuario desde el json a partir de su username
        var usuario = Funciones.getUserByUsername(this,nombre.text.toString())
        if (usuario != null) {
            //Usuario encontrado
            println("User found: ${usuario.nombre} ${usuario.restaurante}")
            Toast.makeText(this, "¡Bienvenido de vuelta!", Toast.LENGTH_SHORT).show()

            //Setear sesion
            setSesion(usuario)

            //Ingresar
            val intent = Intent(this, Opciones::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Usuario no encontrado. Verifica tus credenciales", Toast.LENGTH_SHORT).show()
        }
    }

    fun setSesion(usuario: Usuario){
        Sesion.userName = usuario.userName
        Sesion.nombre = usuario.nombre
        Sesion.apellido = usuario.apellido
        Sesion.email = usuario.email
        if(usuario.restaurante.nombre != ""){
            Sesion.esRestaurante = true
            if (usuario.restaurante.nombre.isNotEmpty()) {
                Sesion.esRestaurante = true
                Sesion.restaurante = mapOf(
                    "nombre" to usuario.restaurante.nombre,
                    "categoria" to usuario.restaurante.categoria,
                    "calificacion" to usuario.restaurante.calificacion,
                    "longitud" to usuario.restaurante.longitud,
                    "latitud" to usuario.restaurante.latitud
                )
            }
        }else if(usuario.restaurante.nombre == ""){
            Sesion.esRestaurante = false
            Sesion.restaurante = mapOf(
                "nombre" to "",
                "categoria" to "",
                "calificacion" to 0.0,
                "longitud" to 0.0,
                "latitud" to 0.0
            )
        }
        // Imprimir sesion con un log
        Log.d("Sesion", "userName: ${Sesion.userName}")
        Log.d("Sesion", "nombre: ${Sesion.nombre}")
        Log.d("Sesion", "apellido: ${Sesion.apellido}")
        Log.d("Sesion", "email: ${Sesion.email}")
        Log.d("Sesion", "esRestaurante: ${Sesion.esRestaurante}")
        Log.d("Sesion", "restaurante: ${Sesion.restaurante}")
    }
}