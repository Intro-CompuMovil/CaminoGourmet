package com.example.camino_gourmet.logic

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.camino_gourmet.R
import com.example.camino_gourmet.data.Restaurant

class RestaurantsAdapter(context: Context?, private val restaurantes: List<Restaurant>): ArrayAdapter<Restaurant>(context!!, 0, restaurantes) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Obtener el restaurante en la posición actual
        val restaurante = getItem(position)

        // Usar el convertView para reutilizar la vista si es posible, si no, inflar una nueva
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.layout_restaurants, parent, false)

        // Obtener las referencias a las vistas dentro del layout
        val nombreRestaurante = view.findViewById<TextView>(R.id.Restaurante)
        val categoriaRestaurante = view.findViewById<ImageView>(R.id.logo)
        val calificacionRestaurante = view.findViewById<TextView>(R.id.Calificacion)
        val perfilButton = view.findViewById<TextView>(R.id.Perfil)

        // Poblar los datos del restaurante en las vistas
        nombreRestaurante.text = restaurante?.nombre
        calificacionRestaurante.text = restaurante?.calificacion.toString()
        when(restaurante?.categoria){
            "Pizza" -> categoriaRestaurante.setImageResource(R.drawable.pizza)
            "Hamburguesa"-> categoriaRestaurante.setImageResource(R.drawable.hamburegesa)
            "Sushi" -> categoriaRestaurante.setImageResource(R.drawable.sushi)
        }

        perfilButton.setOnClickListener {
            val intent = Intent(context, PerfilRestaurante::class.java)
            intent.putExtra("restaurantName", restaurante?.nombre)
            intent.putExtra("puntaje",restaurante?.calificacion)
            context.startActivity(intent)
        }

        return view
    }


}