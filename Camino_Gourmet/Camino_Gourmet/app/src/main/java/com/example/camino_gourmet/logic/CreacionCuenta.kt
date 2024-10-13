package com.example.camino_gourmet.logic

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.camino_gourmet.R
import com.example.camino_gourmet.data.Funciones
import com.example.camino_gourmet.data.Restaurante
import com.example.camino_gourmet.data.Usuario
import kotlin.random.Random

class
CreacionCuenta: AppCompatActivity() {
    lateinit var botonSoyRestaurante: Button
    lateinit var textIniciarSesion : TextView
    lateinit var botonCrearCuenta : Button
    lateinit var nombre : EditText
    lateinit var apellido : EditText
    lateinit var correo : EditText
    lateinit var usuario : EditText
    lateinit var contrasena : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()*/
        setContentView(R.layout.creacion_cuenta)

        //Inicializacion de vistas
        textIniciarSesion = findViewById<TextView>(R.id.InicioSesion)
        botonCrearCuenta = findViewById<Button>(R.id.BotonCrearCuenta)
        nombre = findViewById<EditText>(R.id.Nombre)
        apellido = findViewById<EditText>(R.id.Apellido)
        correo = findViewById<EditText>(R.id.Correo)
        usuario = findViewById<EditText>(R.id.NomUsuario)
        contrasena = findViewById<EditText>(R.id.Contraseña)
        botonSoyRestaurante = findViewById(R.id.soyRestaurante)

        botonSoyRestaurante.setOnClickListener {clickSoyRestaurante()}

        //Crear listener para cuando se haga click en el TextView
        textIniciarSesion.setOnClickListener {
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }

        botonCrearCuenta.setOnClickListener {
            validarCampos()
        }
    }

    fun clickSoyRestaurante(){
        val intent = Intent(this, CreacionCuentaRestaurante::class.java)
        startActivity(intent)
    }

    fun validarCampos(){
        val nombreText = nombre.text.toString()
        val apellidoText = apellido.text.toString()
        val correoText = correo.text.toString()
        val usuarioText = usuario.text.toString()
        val contrasenaText = contrasena.text.toString()

        if(nombreText.isNotEmpty() && apellidoText.isNotEmpty() && correoText.isNotEmpty() && usuarioText.isNotEmpty() && contrasenaText.isNotEmpty()){
            //Crear nuevo restaurante con valores por defecto
            var nuevoRestaurante = Restaurante("","",0.0,0.0,0.0)

            //Crear nuevo usuario con los valores introducidos
            var nuevoUsuario = Usuario(Random.nextInt(1000, 10000),usuarioText,nombreText,apellidoText,correoText,nuevoRestaurante)

            //Crear nuevo objeto de usuario
            var nuevoObjeto = Funciones.createNewUser(nuevoUsuario)

            //Agregar usuario al json en internal storage
            Funciones.addNewUserToUsuarios(this,nuevoObjeto)

            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }
        else
            Toast.makeText(this,"Ingrese los campos para continuar", Toast.LENGTH_SHORT).show()
    }
}

