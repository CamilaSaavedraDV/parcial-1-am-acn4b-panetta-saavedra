package com.example.astra;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Dinamico {

    private static final Juego[] OFERTAS = {
            new Juego("Hollow Knight", "Aventura | Metroidvania", 8.99, "hollow"),
            new Juego("Stardew Valley", "Simulador | RPG", 11.24, "stardew_valley"),
            new Juego("Call of Duty", "Acción | FPS", 38.99, "callofdutty"),
            new Juego("Terraria", "Aventura | Sandbox", 4.99, "terraria")
    };

    private static final String[] DESCUENTOS = {
            "-40%",
            "-25%",
            "-35%",
            "-50%"
    };

    public static void iniciar(Activity activity) {

        LinearLayout contenedor = activity.findViewById(R.id.contenedorOfertas);
        EditText buscador = activity.findViewById(R.id.buscador);
        TextView verTodos = activity.findViewById(R.id.verTodos);

        cargarOfertas(activity, contenedor);
        configurarBuscador(buscador, contenedor);

        verTodos.setOnClickListener(v -> {
            buscador.setText("");
            Toast.makeText(activity, R.string.ver_todos_mensaje, Toast.LENGTH_SHORT).show();
        });
    }

    private static void cargarOfertas(Activity activity, LinearLayout contenedor) {

        contenedor.removeAllViews();

        LinearLayout fila = null;

        for (int i = 0; i < OFERTAS.length; i++) {

            if (i % 2 == 0) {

                fila = new LinearLayout(activity);
                fila.setOrientation(LinearLayout.HORIZONTAL);
                fila.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams filaParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                filaParams.setMargins(0, 0, 0, 8);

                contenedor.addView(fila, filaParams);
            }

            final Juego juego = OFERTAS[i];

            View tarjeta = LayoutInflater.from(activity)
                    .inflate(R.layout.item_oferta, fila, false);

            ImageView imagen = tarjeta.findViewById(R.id.imgJuego);
            TextView nombre = tarjeta.findViewById(R.id.tvNombreJuego);
            TextView genero = tarjeta.findViewById(R.id.tvGenero);
            TextView descuento = tarjeta.findViewById(R.id.tvDescuento);
            TextView precio = tarjeta.findViewById(R.id.tvPrecio);
            TextView agregar = tarjeta.findViewById(R.id.btnAgregarJuego);

            imagen.setImageResource(juego.getImagenRes(activity));
            nombre.setText(juego.getNombre());
            genero.setText(juego.getGenero());
            descuento.setText(DESCUENTOS[i]);
            precio.setText(juego.getPrecioTexto());

            agregar.setOnClickListener(v -> {
                if (!Carrito.agregar(juego)) {
                    Toast.makeText(
                            activity,
                            activity.getString(R.string.juego_ya_en_carrito, juego.getNombre()),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                CarritoDialog.actualizarBadge(activity);

                Toast.makeText(
                        activity,
                        activity.getString(R.string.juego_agregado, juego.getNombre()),
                        Toast.LENGTH_SHORT
                ).show();
            });

            // Pasa el juego a la pantalla de detalle con un extra
            tarjeta.setOnClickListener(v -> {
                Intent intent = new Intent(activity, DetalleJuegoActivity.class);
                intent.putExtra(DetalleJuegoActivity.EXTRA_JUEGO, juego);
                activity.startActivity(intent);
            });

            LinearLayout.LayoutParams tarjetaParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    );

            tarjetaParams.setMargins(3, 0, 3, 0);

            fila.addView(tarjeta, tarjetaParams);
        }
    }

    private static void configurarBuscador(
            EditText buscador,
            LinearLayout contenedor
    ) {

        buscador.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String texto = s.toString().trim().toLowerCase();

                for (int filaIndex = 0; filaIndex < contenedor.getChildCount(); filaIndex++) {

                    LinearLayout fila = (LinearLayout) contenedor.getChildAt(filaIndex);

                    for (int tarjetaIndex = 0; tarjetaIndex < fila.getChildCount(); tarjetaIndex++) {

                        View tarjeta = fila.getChildAt(tarjetaIndex);

                        int posicion = filaIndex * 2 + tarjetaIndex;

                        if (posicion >= OFERTAS.length) {
                            continue;
                        }

                        boolean coincide =
                                OFERTAS[posicion].getNombre().toLowerCase().contains(texto)
                                        || OFERTAS[posicion].getGenero().toLowerCase().contains(texto);

                        tarjeta.setVisibility(coincide ? View.VISIBLE : View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}