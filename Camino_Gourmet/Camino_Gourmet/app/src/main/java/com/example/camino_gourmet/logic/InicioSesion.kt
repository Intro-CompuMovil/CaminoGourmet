package com.example.camino_gourmet.logic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.camino_gourmet.R

class InicioSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()*/
        setContentView(R.layout.inicio_sesion)

        val contrasena = findViewById<EditText>(R.id.Contraseña)
        val nombre = findViewById<EditText>(R.id.Usuario)
        val TextView = findViewById<TextView>(R.id.CrearCuenta)
        val Button = findViewById<Button>(R.id.BotonIngreso)


        TextView.setOnClickListener {
            val intent = Intent(this, CreacionCuenta::class.java)
            startActivity(intent)
        }

        Button.setOnClickListener {
            val nombre = nombre.text.toString()
            val contrasena = contrasena.text.toString()

            if(nombre.isNotEmpty() && contrasena.isNotEmpty()){
                val intent = Intent(this, Opciones::class.java)
                startActivity(intent)
            }
            else
                Toast.makeText(this,"Ingrese los campos para continuar", Toast.LENGTH_SHORT).show()


        }



        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        */

    }
}