package com.example.astra

import android.os.Bundle
import android.content.Intent
import android.view.View
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dinamico.iniciar(this)

        findViewById<View>(R.id.navBiblioteca).setOnClickListener {
            startActivity(Intent(this, BibliotecaActivity::class.java))
        }
    }
}
