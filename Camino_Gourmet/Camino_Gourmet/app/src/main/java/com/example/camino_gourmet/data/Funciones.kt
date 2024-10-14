package com.example.camino_gourmet.data


import android.content.Context
import android.util.Log
import com.example.camino_gourmet.data.Data.Companion.RADIUS_OF_EARTH_KM
import org.json.JSONObject
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import kotlin.math.roundToInt

class Funciones {
    companion object {
        fun guardarRestaurantesjson(context: Context, categoriaSeleccionada: String) {
            val jsonString = loadJSONFromAsset(context)

            if (jsonString != null) {
                try {
                    val json = JSONObject(jsonString)
                    val restuarantesJson = json.getJSONArray("restaurantes")

                    // Limpia la lista antes de llenarla, por si ya tiene datos
                    Data.RESTAURANT_LIST.clear()

                    for (i in 0 until restuarantesJson.length()) {
                        val jsonObject = restuarantesJson.getJSONObject(i)
                        val restaurante = Restaurant(
                            id = jsonObject.getInt("id"),
                            nombre = jsonObject.getString("nombre"),
                            categoria = jsonObject.getString("categoria"),
                            calificacion = jsonObject.getDouble("calificacion"),
                            longitud = jsonObject.getDouble("longitud"),
                            latitud = jsonObject.getDouble("latitud")
                        )

                        //Filtrar por Categoria
                        if (restaurante.categoria.equals(categoriaSeleccionada, ignoreCase = true)) {
                            Data.RESTAURANT_LIST.add(restaurante)
                        }
                    }
                    Log.d("Funciones", "Destinos cargados exitosamente: ${Data.RESTAURANT_LIST.size}")
                } catch (e: Exception) {
                    Log.e("Funciones", "Error procesando el JSON: ${e.message}")
                }
            } else {
                // Manejar el caso cuando el JSON no se puede cargar
                Log.e("Funciones", "No se pudo cargar el archivo destinos.json")
            }
        }

        fun loadJSONFromAsset(context: Context): String? {
            return try {
                val isStream: InputStream = context.assets.open("restaurantes.json")
                val size: Int = isStream.available()
                val buffer = ByteArray(size)
                isStream.read(buffer)
                isStream.close()
                String(buffer, Charsets.UTF_8)
            } catch (ex: IOException) {
                Log.e("Funciones", "Error leyendo el archivo destinos.json: ${ex.message}")
                null
            }
        }

        fun distance(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
            val latDistance = Math.toRadians(lat1 - lat2)
            val lngDistance = Math.toRadians(long1 - long2)
            val a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2))
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            val result = RADIUS_OF_EARTH_KM * c
            return (result * 100.0).roundToInt() / 100.0

        }
    }
}

