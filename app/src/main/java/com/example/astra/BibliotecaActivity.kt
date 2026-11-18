package com.example.astra

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class BibliotecaActivity : ComponentActivity() {

    private data class Juego(
        val nombre: String,
        val genero: String,
        val imagen: Int,
        var instalado: Boolean,
        var deseado: Boolean
    )

    private enum class Filtro { TODOS, INSTALADOS, DESEADOS }

    private val juegos = mutableListOf(
        Juego("Celeste", "Plataformas", R.drawable.celeste_library, instalado = false, deseado = true),
        Juego("Cyberpunk 2077", "RPG", R.drawable.cyberpunk_library, instalado = true, deseado = false),
        Juego("Dead Cells", "Roguelike", R.drawable.dead_cells_library, instalado = true, deseado = false),
        Juego("God of War", "Aventura | Acción", R.drawable.god_of_war_library, instalado = false, deseado = false)
    )

    private lateinit var contenedor: LinearLayout
    private lateinit var mensajeVacio: TextView
    private lateinit var buscador: EditText
    private var filtroActual = Filtro.TODOS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblioteca)

        contenedor = findViewById(R.id.contenedorBiblioteca)
        mensajeVacio = findViewById(R.id.tvBibliotecaVacia)
        buscador = findViewById(R.id.etBuscarBiblioteca)

        val filtroTodos = findViewById<TextView>(R.id.filtroTodos)
        val filtroInstalados = findViewById<TextView>(R.id.filtroInstalados)
        val filtroDeseados = findViewById<TextView>(R.id.filtroDeseados)

        filtroTodos.setOnClickListener {
            filtroActual = Filtro.TODOS
            actualizarBiblioteca(filtroTodos, filtroInstalados, filtroDeseados)
        }
        filtroInstalados.setOnClickListener {
            filtroActual = Filtro.INSTALADOS
            actualizarBiblioteca(filtroTodos, filtroInstalados, filtroDeseados)
        }
        filtroDeseados.setOnClickListener {
            filtroActual = Filtro.DESEADOS
            actualizarBiblioteca(filtroTodos, filtroInstalados, filtroDeseados)
        }

        buscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mostrarJuegos()
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        // Encabezado (notificaciones, carrito, perfil) y barra de abajo
        Navegacion.configurar(this, Navegacion.BIBLIOTECA)

        actualizarBiblioteca(filtroTodos, filtroInstalados, filtroDeseados)
    }

    override fun onResume() {
        super.onResume()
        CarritoDialog.actualizarBadge(this)
    }

    private fun actualizarBiblioteca(
        todos: TextView,
        instalados: TextView,
        deseados: TextView
    ) {
        actualizarEstiloFiltro(todos, filtroActual == Filtro.TODOS)
        actualizarEstiloFiltro(instalados, filtroActual == Filtro.INSTALADOS)
        actualizarEstiloFiltro(deseados, filtroActual == Filtro.DESEADOS)
        mostrarJuegos()
    }

    private fun actualizarEstiloFiltro(vista: TextView, seleccionado: Boolean) {
        vista.setBackgroundResource(
            if (seleccionado) R.drawable.bg_boton_comprar else R.drawable.bg_filtro
        )
        vista.setTextColor(getColor(R.color.texto_principal))
        vista.setTypeface(null, if (seleccionado) android.graphics.Typeface.BOLD else android.graphics.Typeface.NORMAL)
    }

    private fun mostrarJuegos() {
        contenedor.removeAllViews()
        val texto = buscador.text.toString().trim().lowercase()
        val filtrados = juegos.filter { juego ->
            val coincideFiltro = when (filtroActual) {
                Filtro.TODOS -> true
                Filtro.INSTALADOS -> juego.instalado
                Filtro.DESEADOS -> juego.deseado
            }
            coincideFiltro && (juego.nombre.lowercase().contains(texto) || juego.genero.lowercase().contains(texto))
        }

        mensajeVacio.visibility = if (filtrados.isEmpty()) View.VISIBLE else View.GONE
        filtrados.forEach { juego -> agregarTarjeta(juego) }
    }

    private fun agregarTarjeta(juego: Juego) {
        val tarjeta = LayoutInflater.from(this)
            .inflate(R.layout.item_biblioteca_juego, contenedor, false)

        tarjeta.findViewById<android.widget.ImageView>(R.id.imgJuegoBiblioteca).apply {
            setImageResource(juego.imagen)
            contentDescription = juego.nombre
        }
        tarjeta.findViewById<TextView>(R.id.tvNombreJuegoBiblioteca).text = juego.nombre
        tarjeta.findViewById<TextView>(R.id.tvGeneroJuegoBiblioteca).text = juego.genero

        val estado = tarjeta.findViewById<TextView>(R.id.btnEstadoInstalacion)
        val actualizarEstado = {
            estado.text = getString(if (juego.instalado) R.string.estado_instalado else R.string.accion_instalar)
            estado.alpha = if (juego.instalado) 0.78f else 1f
        }
        actualizarEstado()
        estado.setOnClickListener {
            juego.instalado = !juego.instalado
            actualizarEstado()
            Toast.makeText(
                this,
                getString(if (juego.instalado) R.string.juego_instalado else R.string.juego_desinstalado, juego.nombre),
                Toast.LENGTH_SHORT
            ).show()
            if (filtroActual == Filtro.INSTALADOS && !juego.instalado) mostrarJuegos()
        }

        val favorito = tarjeta.findViewById<TextView>(R.id.btnDeseado)
        favorito.text = if (juego.deseado) "★" else "☆"
        favorito.setOnClickListener {
            juego.deseado = !juego.deseado
            favorito.text = if (juego.deseado) "★" else "☆"
            Toast.makeText(
                this,
                getString(if (juego.deseado) R.string.juego_a_deseados else R.string.juego_de_deseados, juego.nombre),
                Toast.LENGTH_SHORT
            ).show()
            if (filtroActual == Filtro.DESEADOS && !juego.deseado) mostrarJuegos()
        }

        contenedor.addView(tarjeta)
    }
}