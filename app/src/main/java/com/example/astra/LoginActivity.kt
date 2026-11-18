package com.example.astra

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {

    companion object {
        private const val USUARIO_VALIDO = "astra"
        private const val CONTRASENA_VALIDA = "astra123"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usuario = findViewById<EditText>(R.id.etUsuario)
        val contrasena = findViewById<EditText>(R.id.etContrasena)
        val botonMostrarContrasena = findViewById<ImageView>(R.id.btnMostrarContrasena)
        var mostrarContrasena = false

        botonMostrarContrasena.setOnClickListener {
            mostrarContrasena = !mostrarContrasena
            val cursor = contrasena.selectionStart
            contrasena.transformationMethod = if (mostrarContrasena) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            contrasena.setSelection(cursor.coerceAtLeast(0).coerceAtMost(contrasena.length()))
            botonMostrarContrasena.setImageResource(
                if (mostrarContrasena) R.drawable.ic_eye_login else R.drawable.ic_eye_off_login
            )
            botonMostrarContrasena.contentDescription = getString(
                if (mostrarContrasena) R.string.ocultar_contrasena else R.string.mostrar_contrasena
            )
        }

        findViewById<android.view.View>(R.id.btnAccesoGoogle).setOnClickListener {
            Toast.makeText(this, R.string.google_no_configurado, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnIniciarSesion).setOnClickListener {
            val usuarioIngresado = usuario.text.toString().trim()
            val contrasenaIngresada = contrasena.text.toString()

            if (usuarioIngresado.isBlank()) {
                usuario.error = getString(R.string.error_usuario)
                usuario.requestFocus()
                return@setOnClickListener
            }

            if (contrasenaIngresada.isBlank()) {
                contrasena.error = getString(R.string.error_contrasena)
                contrasena.requestFocus()
                return@setOnClickListener
            }

            if (!usuarioIngresado.equals(USUARIO_VALIDO, ignoreCase = true) ||
                contrasenaIngresada != CONTRASENA_VALIDA
            ) {
                usuario.error = getString(R.string.error_credenciales)
                usuario.requestFocus()
                return@setOnClickListener
            }

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
