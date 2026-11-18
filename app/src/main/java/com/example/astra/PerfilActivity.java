package com.example.astra;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.core.content.ContextCompat;

public class PerfilActivity extends ComponentActivity {

    private static final int[] ICONOS = {
            R.drawable.ic_heart,
            R.drawable.ic_history,
            R.drawable.ic_settings,
            R.drawable.ic_support,
            R.drawable.ic_logout
    };

    private static final int[] TEXTOS = {
            R.string.perfil_deseados,
            R.string.perfil_historial,
            R.string.perfil_configuracion,
            R.string.perfil_ayuda,
            R.string.perfil_cerrar_sesion
    };

    private TextView tvNombre;
    private TextView tvUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Navegacion.configurar(this, Navegacion.NINGUNO);

        tvNombre = findViewById(R.id.tvNombrePerfil);
        tvUsuario = findViewById(R.id.tvUsuarioPerfil);

        actualizarDatos();

        Button editar = findViewById(R.id.btnEditarPerfil);
        editar.setOnClickListener(v -> mostrarEditarPerfil());

        cargarOpciones();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CarritoDialog.actualizarBadge(this);
    }

    private void actualizarDatos() {
        tvNombre.setText(Usuario.nombre);
        tvUsuario.setText("@" + Usuario.usuario);
    }

    private void cargarOpciones() {

        LinearLayout lista = findViewById(R.id.contenedorOpciones);

        for (int i = 0; i < TEXTOS.length; i++) {

            if (i > 0) {
                View linea = new View(this);
                linea.setBackgroundColor(ContextCompat.getColor(this, R.color.violeta_claro));
                lista.addView(linea, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1));
            }

            View fila = LayoutInflater.from(this)
                    .inflate(R.layout.item_opcion_perfil, lista, false);

            ImageView icono = fila.findViewById(R.id.imgOpcion);
            TextView texto = fila.findViewById(R.id.tvOpcion);

            icono.setImageResource(ICONOS[i]);
            texto.setText(TEXTOS[i]);

            final int posicion = i;

            fila.setOnClickListener(v -> {
                if (posicion == TEXTOS.length - 1) {
                    confirmarCerrarSesion();
                } else {
                    Toast.makeText(
                            this,
                            getString(R.string.seccion_proximamente, getString(TEXTOS[posicion])),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

            lista.addView(fila);
        }
    }

    private void mostrarEditarPerfil() {

        LinearLayout formulario = new LinearLayout(this);
        formulario.setOrientation(LinearLayout.VERTICAL);

        int margen = getResources().getDimensionPixelSize(R.dimen.margen_pantalla);
        formulario.setPadding(margen, margen, margen, 0);

        EditText etNombre = new EditText(this);
        etNombre.setHint(R.string.hint_nombre);
        etNombre.setText(Usuario.nombre);
        formulario.addView(etNombre);

        EditText etUsuario = new EditText(this);
        etUsuario.setHint(R.string.hint_usuario);
        etUsuario.setText(Usuario.usuario);
        formulario.addView(etUsuario);

        new AlertDialog.Builder(this)
                .setTitle(R.string.editar_perfil)
                .setView(formulario)
                .setPositiveButton(R.string.guardar, (dialog, which) -> {

                    String nuevoNombre = etNombre.getText().toString().trim();
                    String nuevoUsuario = etUsuario.getText().toString().trim().replace("@", "");

                    if (nuevoNombre.isEmpty() || nuevoUsuario.isEmpty()) {
                        Toast.makeText(this, R.string.datos_vacios, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Usuario.nombre = nuevoNombre;
                    Usuario.usuario = nuevoUsuario;
                    actualizarDatos();

                    Toast.makeText(this, R.string.perfil_actualizado, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void confirmarCerrarSesion() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.perfil_cerrar_sesion)
                .setMessage(R.string.cerrar_sesion_mensaje)
                .setPositiveButton(R.string.perfil_cerrar_sesion, (dialog, which) -> {

                    Carrito.getJuegos().clear();

                    Toast.makeText(this, R.string.sesion_cerrada, Toast.LENGTH_SHORT).show();

                    // Vuelve al Login y borra las pantallas anteriores
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }
}
