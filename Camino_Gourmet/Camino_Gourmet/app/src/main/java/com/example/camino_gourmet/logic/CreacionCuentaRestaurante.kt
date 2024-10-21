package com.example.camino_gourmet.logic

import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
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
import com.example.camino_gourmet.data.Data
import com.example.camino_gourmet.data.Funciones
import com.example.camino_gourmet.data.Restaurante
import com.example.camino_gourmet.data.Sesion
import com.example.camino_gourmet.data.Usuario
import java.util.Locale
import kotlin.random.Random

class CreacionCuentaRestaurante : AppCompatActivity() {

    private lateinit var textIniciarSesion : TextView
    private lateinit var botonCrearCuentaRestaurante : Button
    private lateinit var nombre : EditText
    private lateinit var apellido : EditText
    private lateinit var correo : EditText
    private lateinit var usuario : EditText
    private lateinit var contrasena : EditText
    private lateinit var nombreRestaurante : EditText
    private lateinit var spinnerCategoria : Spinner
    private lateinit var ubicacion : EditText
    private lateinit var botonMapa: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creacion_cuenta_restaurante)
        textIniciarSesion = findViewById(R.id.InicioSesion)
        botonCrearCuentaRestaurante = findViewById(R.id.BotonCrearCuenta)
        spinnerCategoria = findViewById(R.id.spinnerCategoria)
        nombre = findViewById(R.id.Nombre)
        apellido = findViewById(R.id.Apellido)
        correo = findViewById(R.id.Correo)
        usuario = findViewById(R.id.NomUsuario)
        contrasena = findViewById(R.id.Contraseña)
        nombreRestaurante = findViewById(R.id.nombreRestaurante)
        ubicacion = findViewById(R.id.Ubicacion)
        botonMapa = findViewById(R.id.BotonMapa)

        
        ubicacion.isEnabled = false
        ubicacion.isClickable = false

        if (Data.latitud != null && Data.longitud != null){
            var direction = Data.longitud?.let { Data.latitud?.let { it1 -> getLocationText(it1, it) } }
            ubicacion.setText(direction)
        }


        textIniciarSesion.setOnClickListener {
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }

        botonCrearCuentaRestaurante.setOnClickListener{
            validarCampos()
        }

        botonMapa.setOnClickListener {
            val intent = Intent(this, MapaRestaurante::class.java)
            startActivity(intent)
        }
    }

    private fun validarCampos(){
        val nombreText = nombre.text.toString()
        val apellidoText = apellido.text.toString()
        val correoText = correo.text.toString()
        val usuarioText = usuario.text.toString()
        val contrasenaText = contrasena.text.toString()
        val nombreRestauranteText = nombreRestaurante.text.toString()
        val ubicacionText = ubicacion.text.toString()
        if(nombreText.isNotEmpty() && apellidoText.isNotEmpty() && correoText.isNotEmpty() && usuarioText.isNotEmpty() && contrasenaText.isNotEmpty() && nombreRestauranteText.isNotEmpty() && ubicacionText.isNotEmpty() && Data.latitud != null && Data.longitud != null){
            //Crear nuevo restaurante
            var nuevoRestaurante =
                Data.latitud?.let {
                    Data.longitud?.let { it1 ->
                        Restaurante(nombreRestauranteText,spinnerCategoria.selectedItem.toString(),0.0,
                            it1,
                            it
                        )
                    }
                }

            //Crear nuevo usuario con los valores introducidos
            var nuevoUsuario = nuevoRestaurante?.let {
                Usuario(Random.nextInt(1000, 10000),usuarioText,nombreText,apellidoText,correoText,
                    it
                )
            }

            //Crear nuevo objeto de usuario
            var nuevoObjeto = nuevoUsuario?.let { Funciones.createNewUser(it) }

            //Agregar usuario al json en internal storage
            if (nuevoObjeto != null) {
                Funciones.addNewUserToUsuarios(this,nuevoObjeto)
            }
            Toast.makeText(this,"Cuenta existosamente creada", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)

        }else
            Toast.makeText(this,"Ingrese los campos para continuar", Toast.LENGTH_SHORT).show()

    }

    private fun getLocationText(latitude: Double, longitude: Double): String {
        var locationText = "No se pudo obtener la ubicación"  // Texto por defecto

        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                locationText = address.getAddressLine(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return locationText
    }
}