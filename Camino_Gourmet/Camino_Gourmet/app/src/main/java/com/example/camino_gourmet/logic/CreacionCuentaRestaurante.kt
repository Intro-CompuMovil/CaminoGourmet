package com.example.camino_gourmet.logic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.camino_gourmet.R
import com.example.camino_gourmet.data.Funciones
import com.example.camino_gourmet.data.Restaurante
import com.example.camino_gourmet.data.Usuario
import kotlin.random.Random

class CreacionCuentaRestaurante : AppCompatActivity() {

    lateinit var textIniciarSesion : TextView
    lateinit var botonCrearCuentaRestaurante : Button
    lateinit var nombre : EditText
    lateinit var apellido : EditText
    lateinit var correo : EditText
    lateinit var usuario : EditText
    lateinit var contrasena : EditText
    lateinit var nombreRestaurante : EditText
    lateinit var latitud : EditText
    lateinit var longitud : EditText
    lateinit var spinnerCategoria : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creacion_cuenta_restaurante)
        textIniciarSesion = findViewById<TextView>(R.id.InicioSesion)
        botonCrearCuentaRestaurante = findViewById<Button>(R.id.BotonCrearCuenta)
        spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
        nombre = findViewById<EditText>(R.id.Nombre)
        apellido = findViewById<EditText>(R.id.Apellido)
        correo = findViewById<EditText>(R.id.Correo)
        usuario = findViewById<EditText>(R.id.NomUsuario)
        contrasena = findViewById<EditText>(R.id.Contraseña)
        nombreRestaurante = findViewById<EditText>(R.id.nombreRestaurante)
        latitud = findViewById<EditText>(R.id.latitud)
        longitud = findViewById<EditText>(R.id.longitud)

        textIniciarSesion.setOnClickListener {
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }

        botonCrearCuentaRestaurante.setOnClickListener{
            validarCampos()
        }
    }

    fun validarCampos(){
        val nombreText = nombre.text.toString()
        val apellidoText = apellido.text.toString()
        val correoText = correo.text.toString()
        val usuarioText = usuario.text.toString()
        val contrasenaText = contrasena.text.toString()
        val nombreRestauranteText = nombreRestaurante.text.toString()
        val latitudText = latitud.text.toString()
        val longitudText = longitud.text.toString()
        if(nombreText.isNotEmpty() && apellidoText.isNotEmpty() && correoText.isNotEmpty() && usuarioText.isNotEmpty() && contrasenaText.isNotEmpty() && nombreRestauranteText.isNotEmpty() && latitudText.isNotEmpty() && longitudText.isNotEmpty()){
            //Crear nuevo restaurante
            var nuevoRestaurante = Restaurante(nombreRestauranteText,spinnerCategoria.getSelectedItem().toString(),0.0,longitudText.toDoubleOrNull() ?: 0.0,latitudText.toDoubleOrNull() ?: 0.0)

            //Crear nuevo usuario con los valores introducidos
            var nuevoUsuario = Usuario(Random.nextInt(1000, 10000),usuarioText,nombreText,apellidoText,correoText,nuevoRestaurante)

            //Crear nuevo objeto de usuario
            var nuevoObjeto = Funciones.createNewUser(nuevoUsuario)

            //Agregar usuario al json en internal storage
            Funciones.addNewUserToUsuarios(this,nuevoObjeto)

            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)

        }else
            Toast.makeText(this,"Ingrese los campos para continuar", Toast.LENGTH_SHORT).show()

    }
}