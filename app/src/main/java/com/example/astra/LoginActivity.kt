package com.example.astra

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val correo = findViewById<EditText>(R.id.etCorreo)
        val contrasena = findViewById<EditText>(R.id.etContrasena)

        findViewById<Button>(R.id.btnIniciarSesion).setOnClickListener {
            val correoIngresado = correo.text.toString().trim()
            val contrasenaIngresada = contrasena.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(correoIngresado).matches()) {
                correo.error = getString(R.string.error_correo)
                correo.requestFocus()
                return@setOnClickListener
            }

            if (contrasenaIngresada.isBlank()) {
                contrasena.error = getString(R.string.error_contrasena)
                contrasena.requestFocus()
                return@setOnClickListener
            }

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
