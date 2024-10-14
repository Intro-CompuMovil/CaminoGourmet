package com.example.camino_gourmet.logic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.location.Location
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.camino_gourmet.R
import com.example.camino_gourmet.data.Data
import com.example.camino_gourmet.data.Data.Companion.RADIUS_OF_EARTH_KM
import com.example.camino_gourmet.data.Data.Companion.latitude
import com.example.camino_gourmet.data.Data.Companion.longitude
import com.example.camino_gourmet.data.Funciones
import com.example.camino_gourmet.data.Restaurant
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.tasks.Task
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.views.overlay.Polyline
import kotlin.math.roundToInt

class Mapa: AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var Restaurante: String
    private lateinit var boton: Button
    private lateinit var mapView: MapView
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback
    lateinit var roadManager: RoadManager
    private var roadOverlay: Polyline? = null
    private var userMarker: Marker? = null //Variable para almaenar marcador actual
    private var listaMarkerRest = mutableListOf<Marker>() //Almacenar los marcadores de restaurantes
    private val getLocationSettings = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        Log.i("LOCATION", "Result from settings: ${result.resultCode}")
        if (result.resultCode == RESULT_OK) {
            var settingsOK = true
            startLocationUpdates()
        } else {
            statusTextView.text = "GPS is off"
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()*/

        //Inicializar el contexto para osmdroid
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))

        setContentView(R.layout.mapa)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mapView = findViewById(R.id.osmMap)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)
        roadManager = OSRMRoadManager(this, "ANDROID")

        val algo = Data.MY_PERMISSION_LOCATION_CODE

        statusTextView = findViewById(R.id.textView5)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationRequest = createLocationRequest()

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                Log.i("LOCATION", "Location update in the callback: $location")
                if (location != null) {
                    actualizarUbicacion(location)
                }
            }
        }



        // Recibir el tipo de restaurante seleccionado
        Restaurante = intent.getStringExtra("TipoRestaurante") ?: ""
        Funciones.guardarRestaurantesjson(this, Restaurante)
        val Button = findViewById<Button>(R.id.button)
        boton = findViewById<Button>(R.id.botonCentrar)

        statusTextView.text = "Buscando restaurantes de: $Restaurante en la zona"
        statusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))


        pedirPermiso(this, Manifest.permission.ACCESS_FINE_LOCATION, "Acceder Ubicacion", algo)
        pedirPermiso(this, Manifest.permission.ACCESS_COARSE_LOCATION, "Acceder Ubicacion", algo)
        checkLocationSettings()


        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        */

        Button.setOnClickListener {
            val intent = Intent(this, Paradas::class.java).apply {
                putExtra("TipoRestaurante", Restaurante)
            }
            startActivity(intent)
        }

        boton.setOnClickListener {
            //Ubicar el mapa en la ubicación del usuario
            mapView.controller.setZoom(15.0)
            mapView.controller.setCenter(latitude?.let { it1 -> longitude?.let { it2 ->
                GeoPoint(it1,
                    it2
                )
            } })

        }

    }

    private fun actualizarUbicacion(location: Location) {
        val userLocation = GeoPoint(location.latitude, location.longitude)
        latitude = location.latitude
        longitude = location.longitude
        val waypoints = ArrayList<GeoPoint>()
        mapView.overlays.remove(roadOverlay) // Elimina el overlay de la ruta
        mapView.invalidate() // Refresca el mapa


        waypoints.add(userLocation)

        if (userMarker != null) {
            userMarker?.remove(mapView)
        }

        // Añadir un marcador en la ubicación del usuario
        userMarker = Marker(mapView)
        userMarker?.position = userLocation
        userMarker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        userMarker?.title = "Tu ubicación"
        userMarker?.alpha = 1.0f
        mapView.overlays.add(userMarker)

        // Obtener lista ordenada por proximidad
        val sortedRestaurants = getRestaurantsByProximity(location, Data.RESTAURANT_ROUTE)

        // Agregar los restaurantes como puntos en la lista
        sortedRestaurants.forEach { restaurant ->
            waypoints.add(GeoPoint(restaurant.latitud,restaurant.longitud))
        }

        val road = roadManager.getRoad(waypoints)

        if (road.mStatus != Road.STATUS_OK) {
            Toast.makeText(this, "Error al obtener la ruta", Toast.LENGTH_SHORT).show()
            return
        }

        // Calcular tiempo estimado de la ruta
        val duracion = road.mDuration
        val totalDurationInMinutes = duracion / 60
        val redondeado = kotlin.math.round(totalDurationInMinutes)

        statusTextView.text = "Duracion Estimada Recorrido: $redondeado minutos"
        statusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))

        // Dibujar la ruta en el mapa
        if (waypoints.size > 1){
            if (mapView != null) {
                roadOverlay?.let {mapView.overlays.remove(it) }
                roadOverlay = RoadManager.buildRoadOverlay(road)
                roadOverlay?.outlinePaint?.color = Color.MAGENTA
                roadOverlay?.outlinePaint?.strokeWidth = 10f
                mapView.overlays.add(roadOverlay)

                //Refrescar el mapa
                mapView.invalidate()
            }
        }


        boton.setOnClickListener {
            //Ubicar el mapa en la ubicación del usuario
            mapView.controller.setZoom(15.0)
            mapView.controller.setCenter(waypoints[0])

        }
    }


    private fun checkLocationSettings() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            Log.i("LOCATION", "GPS is ON")
            var settingsOK = true
            startLocationUpdates()
        }
    }

    private fun createLocationRequest(): LocationRequest =
        // New builder
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).apply {
            setMinUpdateIntervalMillis(5000)
        }.build()

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var intentCuenta = Intent(this, Perfil::class.java)
        var intentMiRestaurante = Intent(this, MiRestaurante::class.java)
        when (item.itemId) {
            R.id.Cuenta -> startActivity(intentCuenta)
            R.id.miRestaurante -> startActivity(intentMiRestaurante)
            R.id.Inicio -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pedirPermiso(
        context: Activity,
        permiso: String,
        justificacion: String,
        idCode: Int
    ) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                permiso
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permiso ya concedido, usar la ubicación
                setLocation()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(context, permiso) -> {
                // Mostrar justificación y solicitar permiso
                showPermissionRationale()
                ActivityCompat.requestPermissions(context, arrayOf(permiso), idCode)
            }

            else -> {
                // Solicitar permiso directamente
                ActivityCompat.requestPermissions(this, arrayOf(permiso), idCode)
            }
        }
    }


    private fun setLocation() {
        // Verifica permisos antes de intentar acceder a la ubicación
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    BuscarRestaurante(location)
                } else {
                    statusTextView.text = "No se pudo obtener la ubicación."
                    statusTextView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            android.R.color.holo_red_dark
                        )
                    )
                }
            }
        } else {
            // Mostrar estado de permiso denegado
            showPermissionStatus(false)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun BuscarRestaurante(location: Location) {

        val userLocation = GeoPoint(location.latitude, location.longitude)
        val restaurantes = Data.RESTAURANT_LIST

        userMarker?.remove(mapView)

        //Ubicar el mapa en la ubicación del usuario
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(userLocation)

        // Añadir un marcador en la ubicación del usuario
        userMarker = Marker(mapView)
        userMarker?.position = userLocation
        userMarker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        userMarker?.title = "Tu ubicación"
        userMarker?.alpha = 1.0f
        mapView.overlays.add(userMarker)


        for (restaurant in restaurantes) {
            val markerRestaurante = Marker(mapView)
            val point = GeoPoint(restaurant.latitud, restaurant.longitud)
            markerRestaurante.position = point
            markerRestaurante.title = restaurant.nombre
            markerRestaurante.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            markerRestaurante.subDescription =
                "Calificacion de " + restaurant.calificacion.toString()


            markerRestaurante.alpha =
                if (Data.RESTAURANT_ROUTE.contains(restaurant)) 1.0f else 0.09f

            // Añadir el marcador al mapa
            mapView.overlays.add(markerRestaurante)
            markerRestaurante.showInfoWindow()
            listaMarkerRest.add(markerRestaurante)
        }

        //Refrescar el mapa
        mapView.invalidate()
    }

    fun getRestaurantsByProximity(location: Location, restaurantes: ArrayList<Restaurant>): List<Restaurant> {
        var userLat = location.latitude
        var userLong = location.longitude
        return restaurantes.sortedBy { restaurant ->
            Funciones.distance(userLat, userLong, restaurant.latitud, restaurant.longitud)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestCode -> {
                // Si el permiso fue cancelado, el arreglo de permisos esta vacio
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // El permiso fue concedido, usar ubicacion
                    setLocation()
                } else {
                    // Mostrar estado de permiso denegado
                    showPermissionStatus(false)
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun showPermissionRationale() {
        Toast.makeText(
            this, "El permiso de ubicacion es necesario para acceder a tu longitud y latitud",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showPermissionStatus(granted: Boolean) {
        if (!granted) {
            statusTextView.text = "PERMISO DENEGADO!"
            statusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        } else {
            statusTextView.text = ""  // Limpiar el mensaje cuando se cargan los contactos
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        val mapController: IMapController = mapView.controller
        mapController.setZoom(18.0)

        // Inicializa el callback en onResume para que se reinicie la actividad
        checkLocationSettings()

        // Intentar obtener la ubicación y centrar el mapa en la reanudación de la actividad
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setLocation()
        }


        for (restaurant in Data.RESTAURANT_LIST) {
            listaMarkerRest.forEach { marker ->
                marker.alpha = if (Data.RESTAURANT_ROUTE.contains(restaurant)) 1.0f else 0.09f
            }
        }


    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        mapView.onPause()
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

}

