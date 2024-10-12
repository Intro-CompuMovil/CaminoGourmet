package com.example.camino_gourmet.logic

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.camino_gourmet.R

class MiRestaurante : AppCompatActivity() {
    lateinit var switchRestaurante : Switch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_restaurante)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        switchRestaurante = findViewById<Switch>(R.id.switchRestaurante)
        checkSwitch()
        switchRestaurante.setOnClickListener{clickSwitch();checkSwitch()}


    }

    fun clickSwitch(){
        var intentHuella = Intent(this, AutorizarHuella::class.java)
        startActivity(intentHuella)
    }

    fun checkSwitch(){
        var stateChecked = switchRestaurante.isChecked
        if(stateChecked){
            switchRestaurante.text = "Abierto"
            switchRestaurante.setTextColor(resources.getColor(R.color.verde))
        }else{
            switchRestaurante.text = "Cerrado"
            switchRestaurante.setTextColor(resources.getColor(R.color.rojo))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var intentCuenta = Intent(this, Perfil::class.java)
        var intentInicio = Intent(this, Mapa::class.java)
        when(item.itemId){
            R.id.Cuenta -> startActivity(intentCuenta)
            R.id.miRestaurante -> {}
            R.id.Inicio -> startActivity(intentInicio)
        }
        return super.onOptionsItemSelected(item)
    }
}