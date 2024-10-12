package com.example.camino_gourmet.logic

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.camino_gourmet.R

class Opciones: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()*/
        setContentView(R.layout.opciones)

        //Inicializacion de vistas
        val Hamburger = findViewById<ImageView>(R.id.Hamburger)
        val Sushi = findViewById<ImageView>(R.id.Sushi)
        val Pizza = findViewById<ImageView>(R.id.Pizza)

        Hamburger.setOnClickListener {
            TipoRestaurante("Hamburguesa")
        }
        Sushi.setOnClickListener {
            TipoRestaurante("Sushi")
        }
        Pizza.setOnClickListener {
            TipoRestaurante("Pizza")
        }


        /*
          ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
           }
         */

    }

    private fun  TipoRestaurante(tipo: String) {
        val intent = Intent(this, Mapa::class.java).apply {
            putExtra("TipoRestaurante", tipo)
        }
        startActivity(intent)
    }





}