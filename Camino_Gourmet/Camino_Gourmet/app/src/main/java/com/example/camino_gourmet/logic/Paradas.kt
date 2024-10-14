package com.example.camino_gourmet.logic

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.camino_gourmet.R
import com.example.camino_gourmet.data.Data
import com.example.camino_gourmet.data.Funciones
import com.example.camino_gourmet.data.Sesion
import com.example.camino_gourmet.data.Restaurant
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class Paradas: AppCompatActivity() {

    private lateinit var Restaurante: String
    private lateinit var statusTextView: TextView
    private  lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var listView: ListView
    private lateinit var seleccion: Button



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()*/
        setContentView(R.layout.paradas)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val latitude = Data.latitude
        val longitude = Data.longitude
        val algo = Data.MY_PERMISSION_LOCATION_CODE
        seleccion = findViewById(R.id.botonCrearRuta)
        listView = findViewById<ListView>(R.id.lista)
        statusTextView = findViewById(R.id.Ubicacion)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        Restaurante = intent.getStringExtra("TipoRestaurante") ?: ""
        Funciones.guardarRestaurantesjson(this, Restaurante)

        val sortedRestaurants = Data.RESTAURANT_LIST.sortedBy { restaurant ->
            Data.latitude?.let { Data.longitude?.let { it1 ->
                Funciones.distance(it,
                    it1, restaurant.latitud, restaurant.longitud)
            } }
        }

        val adapter =  RestaurantsAdapter(this,sortedRestaurants)
        listView.adapter = adapter


        statusTextView.text = "Ubicado en latitud $latitude y longitud $longitude"
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        */
        seleccion.setOnClickListener {
            val intent = Intent(this, Mapa::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)

        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.drawer_menu, menu)
        //Ocultar boton si el usuario no es restaurante
        menu?.findItem(R.id.miRestaurante)?.isVisible = Sesion.esRestaurante
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var intentCuenta = Intent(this, Perfil::class.java)
        var intentMiRestaurante = Intent(this, MiRestaurante::class.java)
        var intentInicio = Intent(this, Mapa::class.java)
        when(item.itemId){
            R.id.Cuenta -> startActivity(intentCuenta)
            R.id.miRestaurante -> startActivity(intentMiRestaurante)
            R.id.Inicio -> startActivity(intentInicio)
        }
        return super.onOptionsItemSelected(item)
    }


}