package com.example.camino_gourmet.logic

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.camino_gourmet.R

class AutorizarHuella : AppCompatActivity() {
    private val BIOMETRICS_PERMISSION_CODE = 105
    lateinit var resultadoPermiso : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autorizar_huella)
        resultadoPermiso = findViewById(R.id.resultadoPermiso)
        checkAndRequestBiometricsPermission()

    }

    private fun checkAndRequestBiometricsPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.USE_BIOMETRIC
            ) == PackageManager.PERMISSION_GRANTED -> {
                // El permiso ya fue concedido
                initView()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.USE_BIOMETRIC
            ) -> {
                // Mostrar justificación y solicitar permiso
                showPermissionRationale()
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.USE_BIOMETRIC),
                    BIOMETRICS_PERMISSION_CODE
                )
            }
            else -> {
                // Solicitar permiso directamente
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.USE_BIOMETRIC),
                    BIOMETRICS_PERMISSION_CODE
                )
            }
        }
    }

    private fun showPermissionRationale() {
        Toast.makeText(
            this,
            "El permiso de huella es necesario para el funcionamiento de la aplicación.",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            BIOMETRICS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // El permiso fue concedido
                    initView()
                } else {
                    // Mostrar estado de permiso denegado
                    showPermissionStatus(false)
                }
            }
        }
    }

    private fun initView() {
        resultadoPermiso.text = "Permiso concedido"
    }

    private fun showPermissionStatus(granted: Boolean) {
        if (!granted) {
            Toast.makeText(this, "El permiso fue denegado", Toast.LENGTH_LONG).show()
            resultadoPermiso.text = "Permiso denegado"
        } else {

        }
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autorizar_huella)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        val biometricLoginButton =
            findViewById<Button>(R.id.biometric_login)
        biometricLoginButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

    }*/
}