package com.example.camino_gourmet.logic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.camino_gourmet.R
import com.example.camino_gourmet.data.Funciones
import com.example.camino_gourmet.data.Sesion

class EditarCuenta : AppCompatActivity() {
    lateinit var editNombre: EditText
    lateinit var editApellido: EditText
    lateinit var editEmail: EditText
    lateinit var editUsername: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cuenta)
        editNombre = findViewById<EditText>(R.id.editNombre)
        editApellido = findViewById<EditText>(R.id.editApellido)
        editEmail = findViewById<EditText>(R.id.editEmail)
        editUsername = findViewById<EditText>(R.id.editUsername)
        var botonEditarCuenta = findViewById<Button>(R.id.botonEditarCuenta)
        botonEditarCuenta.setOnClickListener{editarCuenta()}
        editNombre.setText(Sesion.nombre)
        editApellido.setText(Sesion.apellido)
        editEmail.setText(Sesion.email)
        editUsername.setText(Sesion.userName)
    }

    fun editarCuenta(){
        //Actualizar sesion
        Sesion.nombre = editNombre.text.toString()
        Sesion.apellido = editApellido.text.toString()
        Sesion.email = editEmail.text.toString()

        //Actualizar json
        Funciones.editUserInUsuarios(this, Sesion.userName ,editNombre.text.toString(), editApellido.text.toString(), editEmail.text.toString(), editUsername.text.toString())

        //Actualizar username en sesion
        Sesion.userName = editUsername.text.toString()

        //Redirigir a perfil
        intent = Intent(this, Perfil::class.java)
        startActivity(intent)
    }
}