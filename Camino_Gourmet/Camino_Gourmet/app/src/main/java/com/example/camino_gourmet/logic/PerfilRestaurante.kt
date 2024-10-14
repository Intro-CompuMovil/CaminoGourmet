package com.example.camino_gourmet.logic

import ComentariosAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.camino_gourmet.R
import com.example.camino_gourmet.data.Comentario
import com.example.camino_gourmet.data.Funciones
import com.example.camino_gourmet.data.Sesion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class PerfilRestaurante : AppCompatActivity() {
    private lateinit var comentariosView: RecyclerView
    private lateinit var adapter: ComentariosAdapter
    lateinit var botonCalificarRestaurante: Button
    lateinit var restaurantName: String
    var calificacion = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_restaurante)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        restaurantName = intent.getStringExtra("restaurantName").toString()
        calificacion = intent.getDoubleExtra("puntaje",0.0)

        val textoNombreRestaurante = findViewById<TextView>(R.id.textoNombreRestaurante)
        val calificacionRestaurante = findViewById<TextView>(R.id.Calificacion)

        textoNombreRestaurante.text = restaurantName
        calificacionRestaurante.text = calificacion.toString()

        comentariosView = findViewById(R.id.comentariosView)
        comentariosView.layoutManager = LinearLayoutManager(this)
        loadComentarios(this)
        botonCalificarRestaurante = findViewById<Button>(R.id.botonCalificarRestaurante)
        botonCalificarRestaurante.setOnClickListener{clickBotonCalificarRestaurante()}

        //Ocultar boton de calificar si el usuario es restaurante
        if(Sesion.esRestaurante == false) {
            botonCalificarRestaurante.visibility = View.VISIBLE
        }else{
            botonCalificarRestaurante.visibility = View.GONE
        }


    }

    fun clickBotonCalificarRestaurante(){
        var bundle = Bundle()
        Log.d("DesdePerfilRestaurante", "restaurantName: $restaurantName")
        Log.d("DesdePerfilRestaurante", "calificacion: $calificacion")
        bundle.putString("restaurantName",restaurantName)
        bundle.putDouble("puntaje",calificacion)
        var intentCalificar = Intent(this, CalificarRestaurante::class.java)
        intentCalificar.putExtras(bundle)
        startActivity(intentCalificar)
    }

    private fun loadComentarios(context: Context) {
        val gson = Gson()
        try {
            // Cargar el archivo JSON desde el almacenamiento interno
            val jsonString = Funciones.loadCommentsJSONFromInternalStorage(context)

            if (jsonString != null) {
                // Parsear el JSON usando Gson
                val comentarioResponse = gson.fromJson(jsonString, ComentarioResponse::class.java)
                // Configurar el adaptador con los comentarios cargados
                adapter = ComentariosAdapter(comentarioResponse.comentarios)
                comentariosView.adapter = adapter
            } else {
                Log.e("loadComentarios", "No se pudo cargar el archivo comentarios.json desde el almacenamiento interno.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private data class ComentarioResponse(
        val comentarios: List<Comentario>
    )

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.drawer_menu, menu)
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