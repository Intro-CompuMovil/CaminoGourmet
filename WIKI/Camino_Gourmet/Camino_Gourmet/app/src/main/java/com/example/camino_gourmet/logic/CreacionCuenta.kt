package com.example.camino_gourmet.logic

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.camino_gourmet.R

class CreacionCuenta: AppCompatActivity() {

    private lateinit var switch: Switch
    private lateinit var restaurante: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()*/
        setContentView(R.layout.creacion_cuenta)

        //Inicializacion de vistas
        restaurante = findViewById<TextView>(R.id.Restaurante)
        switch = findViewById<Switch>(R.id.Switch)

        //Estado inicial del TextView Restaurante
        restaurante.visibility = View.GONE

        //Crear listener para el switch
        switch.setOnClickListener { view ->
            handleSwitchClick(view)
        }



            /*
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            */

    }

    private fun handleSwitchClick(view: View) {
        if (view.id == R.id.Switch) {
            //Mostrar u ocultar el TextView segun el estado del Switch
            if (switch.isChecked)
                restaurante.visibility = View.VISIBLE

            else
                restaurante.visibility = View.GONE
        }
    }

}

