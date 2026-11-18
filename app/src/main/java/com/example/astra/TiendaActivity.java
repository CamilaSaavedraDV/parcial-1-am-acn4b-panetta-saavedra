package com.example.astra;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import java.util.ArrayList;

public class TiendaActivity extends ComponentActivity {

    private final ArrayList<View> tarjetas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);

        Navegacion.configurar(this, Navegacion.TIENDA);

        configurarBanner();

        LinearLayout masVendidos = findViewById(R.id.contenedorMasVendidos);
        LinearLayout gratuitos = findViewById(R.id.contenedorGratuitos);

        cargarTarjetas(Catalogo.masVendidos(), masVendidos);
        cargarTarjetas(Catalogo.gratuitos(), gratuitos);

        configurarBuscador();
        configurarVerTodos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CarritoDialog.actualizarBadge(this);
    }

    private void configurarBanner() {

        final Juego elden = Catalogo.eldenRing();

        ImageView imagen = findViewById(R.id.imgBanner);
        Button verMas = findViewById(R.id.btnVerMasBanner);

        imagen.setImageResource(elden.getImagenRes(this));
        verMas.setOnClickListener(v -> abrirDetalle(elden));
    }

    private void cargarTarjetas(ArrayList<Juego> juegos, LinearLayout contenedor) {

        for (Juego juego : juegos) {

            View tarjeta = LayoutInflater.from(this)
                    .inflate(R.layout.item_juego_tienda, contenedor, false);

            ImageView imagen = tarjeta.findViewById(R.id.imgJuego);
            TextView nombre = tarjeta.findViewById(R.id.tvNombreJuego);
            TextView genero = tarjeta.findViewById(R.id.tvGenero);
            TextView precio = tarjeta.findViewById(R.id.tvPrecio);
            TextView agregar = tarjeta.findViewById(R.id.btnAgregarJuego);

            imagen.setImageResource(juego.getImagenRes(this));
            nombre.setText(juego.getNombre());
            genero.setText(juego.getGenero());

            if (juego.getPrecio() == 0) {
                precio.setText(R.string.precio_gratis);
            } else {
                precio.setText(juego.getPrecioTexto());
            }

            agregar.setOnClickListener(v -> {
                if (Carrito.agregar(juego)) {
                    CarritoDialog.actualizarBadge(this);
                    Toast.makeText(
                            this,
                            getString(R.string.juego_agregado, juego.getNombre()),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            this,
                            getString(R.string.juego_ya_en_carrito, juego.getNombre()),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });

            tarjeta.setOnClickListener(v -> abrirDetalle(juego));
            tarjeta.setTag(juego);

            contenedor.addView(tarjeta);
            tarjetas.add(tarjeta);
        }
    }

    private void abrirDetalle(Juego juego) {
        Intent intent = new Intent(this, DetalleJuegoActivity.class);
        intent.putExtra(DetalleJuegoActivity.EXTRA_JUEGO, juego);
        startActivity(intent);
    }

    private void configurarBuscador() {

        EditText buscador = findViewById(R.id.buscadorTienda);
        TextView sinResultados = findViewById(R.id.tvSinResultados);

        buscador.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String texto = s.toString().trim().toLowerCase();
                int visibles = 0;

                for (View tarjeta : tarjetas) {

                    Juego juego = (Juego) tarjeta.getTag();

                    boolean coincide =
                            juego.getNombre().toLowerCase().contains(texto)
                                    || juego.getGenero().toLowerCase().contains(texto);

                    tarjeta.setVisibility(coincide ? View.VISIBLE : View.GONE);

                    if (coincide) {
                        visibles++;
                    }
                }

                sinResultados.setVisibility(visibles == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void configurarVerTodos() {

        EditText buscador = findViewById(R.id.buscadorTienda);
        TextView verTodosVendidos = findViewById(R.id.verTodosVendidos);
        TextView verTodosGratuitos = findViewById(R.id.verTodosGratuitos);

        View.OnClickListener verTodos = v -> {
            buscador.setText("");
            Toast.makeText(this, R.string.ver_todos_mensaje, Toast.LENGTH_SHORT).show();
        };

        verTodosVendidos.setOnClickListener(verTodos);
        verTodosGratuitos.setOnClickListener(verTodos);
    }
}
