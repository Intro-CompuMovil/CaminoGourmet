package com.example.camino_gourmet.logic

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.camino_gourmet.R
import com.example.camino_gourmet.data.Comentario
import com.example.camino_gourmet.data.Funciones
import com.example.camino_gourmet.data.Sesion
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalificarRestaurante : AppCompatActivity() {

    private var imageBitmap: Bitmap? = null
    private val CAMERA_PERMISSION_CODE = 101
    private val CAMERA_REQUEST_CODE = 102
    private val GALLERY_REQUEST_CODE = 103

    private lateinit var imageView: ImageView
    private lateinit var btnCamara: ImageButton
    private lateinit var btnGaleria: ImageButton
    lateinit var botonCalificar : Button
    lateinit var ratingBar : RatingBar
    lateinit var editComentario : EditText

    lateinit var restaurantName: String
    var calificacion = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calificar_restaurante)


        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        imageView = findViewById(R.id.imageView) // Cambié de TextView a ImageView
        btnCamara = findViewById(R.id.botonCamara)
        btnGaleria = findViewById(R.id.botonAddImage)
        botonCalificar = findViewById(R.id.botonCalificar)
        ratingBar = findViewById(R.id.ratingBar)
        editComentario = findViewById(R.id.editComentario)
        botonCalificar.setOnClickListener{calificar()}

        restaurantName = intent.getStringExtra("restaurantName").toString()
        calificacion = intent.getDoubleExtra("puntaje",0.0)
        Log.d("DesdeCalificar", "restaurantName: $restaurantName")
        Log.d("DesdeCalificar", "calificacion: $calificacion")


        btnCamara.setOnClickListener {
            checkCameraPermission()
        }

        btnGaleria.setOnClickListener {
            openGallery()
        }
    }

    fun calificar(){
        var calificacion = ratingBar.rating
        var contenidoComentario = editComentario.text.toString()
        var nombreCompleto = Sesion.nombre + " " + Sesion.apellido
        var fechaActual = LocalDate.now()
        var formato = DateTimeFormatter.ofPattern("dd-MM-yy")
        var fechaComentario = fechaActual.format(formato)
        var nuevoComentario = Comentario(nombreCompleto, calificacion.toString() + " estrellas", fechaComentario, contenidoComentario)

        var objetoNuevoComentario = Funciones.createNewComment(nuevoComentario)
        Funciones.addNewCommentToComentarios(this, objetoNuevoComentario)

        var bundle = Bundle()
        bundle.putString("restaurantName",restaurantName)
        bundle.putDouble("puntaje", calificacion.toDouble())

        var intent = Intent(this, PerfilRestaurante::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    // Verificar permisos de cámara
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    // Iniciar la cámara
    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    // Abrir galería
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    // Manejar el resultado de la cámara y galería
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(bitmap)
                    imageBitmap = bitmap // Guardar la imagen
                }
                GALLERY_REQUEST_CODE -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let {
                        val inputStream = contentResolver.openInputStream(it)
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        imageView.setImageBitmap(bitmap)
                        imageBitmap = bitmap // Guardar la imagen
                    }
                }
            }
        }
    }

    // Manejo de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
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