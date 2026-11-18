package com.example.astra;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

public class DetalleJuegoActivity extends ComponentActivity {

    public static final String EXTRA_JUEGO = "juego";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_juego);

        final Juego juego = (Juego) getIntent().getSerializableExtra(EXTRA_JUEGO);

        if (juego == null) {
            finish();
            return;
        }

        ImageView imagen = findViewById(R.id.imgDetalle);
        TextView nombre = findViewById(R.id.tvNombreDetalle);
        TextView genero = findViewById(R.id.tvGeneroDetalle);
        TextView precio = findViewById(R.id.tvPrecioDetalle);
        TextView volver = findViewById(R.id.btnVolver);
        Button agregar = findViewById(R.id.btnAgregarDetalle);

        imagen.setImageResource(juego.getImagenRes(this));
        nombre.setText(juego.getNombre());
        genero.setText(juego.getGenero());

        if (juego.getPrecio() == 0) {
            precio.setText(R.string.precio_gratis);
        } else {
            precio.setText(juego.getPrecioTexto());
        }

        volver.setOnClickListener(v -> finish());

        agregar.setOnClickListener(v -> {
            if (Carrito.agregar(juego)) {
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
    }
}
