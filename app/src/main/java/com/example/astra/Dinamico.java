package com.example.astra;

import android.app.Activity;
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

    private static final String[] JUEGOS = {
            "Hollow Knight",
            "Stardew Valley",
            "Call of Duty",
            "Terraria"
    };

    private static final String[] GENEROS = {
            "Metroidvania",
            "Simulación",
            "Acción | FPS",
            "Aventura | Sandbox"
    };

    private static final double[] PRECIOS = {
            8.99,
            11.24,
            9.89,
            4.99
    };

    private static final String[] DESCUENTOS = {
            "-40%",
            "-25%",
            "-35%",
            "-50%"
    };

    private static final int[] IMAGENES = {
            R.drawable.hollow,
            R.drawable.stardew_valley,
            R.drawable.callofdutty,
            R.drawable.terraria
    };

    public static void iniciar(Activity activity) {

        LinearLayout contenedor = activity.findViewById(R.id.contenedorOfertas);
        EditText buscador = activity.findViewById(R.id.buscador);
        TextView verTodos = activity.findViewById(R.id.verTodos);
        ImageView btnNotificaciones = activity.findViewById(R.id.btnNotificaciones);
        ImageView btnCarrito = activity.findViewById(R.id.btnCarrito);

        cargarOfertas(activity, contenedor);
        configurarBuscador(buscador, contenedor);

        verTodos.setOnClickListener(v -> {
            buscador.setText("");
            Toast.makeText(activity, "Mostrando todos los juegos", Toast.LENGTH_SHORT).show();
        });

        btnNotificaciones.setOnClickListener(v -> {
            Toast.makeText(activity, "Notificaciones", Toast.LENGTH_SHORT).show();
        });

        btnCarrito.setOnClickListener(v -> {
            Toast.makeText(activity, "Carrito", Toast.LENGTH_SHORT).show();
        });
    }

    private static void cargarOfertas(Activity activity, LinearLayout contenedor) {

        contenedor.removeAllViews();

        LinearLayout fila = null;

        for (int i = 0; i < JUEGOS.length; i++) {

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

            View tarjeta = LayoutInflater.from(activity)
                    .inflate(R.layout.item_oferta, fila, false);

            ImageView imagen = tarjeta.findViewById(R.id.imgJuego);
            TextView nombre = tarjeta.findViewById(R.id.tvNombreJuego);
            TextView genero = tarjeta.findViewById(R.id.tvGenero);
            TextView descuento = tarjeta.findViewById(R.id.tvDescuento);
            TextView precio = tarjeta.findViewById(R.id.tvPrecio);
            TextView agregar = tarjeta.findViewById(R.id.btnAgregarJuego);

            imagen.setImageResource(IMAGENES[i]);
            nombre.setText(JUEGOS[i]);
            genero.setText(GENEROS[i]);
            descuento.setText(DESCUENTOS[i]);
            precio.setText("$ " + PRECIOS[i]);

            final int posicion = i;

            agregar.setOnClickListener(v ->
                    Toast.makeText(
                            activity,
                            JUEGOS[posicion] + " agregado al carrito",
                            Toast.LENGTH_SHORT
                    ).show()
            );

            tarjeta.setOnClickListener(v ->
                    Toast.makeText(
                            activity,
                            JUEGOS[posicion],
                            Toast.LENGTH_SHORT
                    ).show()
            );

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
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {

                String texto = s.toString().trim().toLowerCase();

                for (int filaIndex = 0;
                     filaIndex < contenedor.getChildCount();
                     filaIndex++) {

                    LinearLayout fila =
                            (LinearLayout) contenedor.getChildAt(filaIndex);

                    for (int tarjetaIndex = 0;
                         tarjetaIndex < fila.getChildCount();
                         tarjetaIndex++) {

                        View tarjeta =
                                fila.getChildAt(tarjetaIndex);

                        int posicion =
                                filaIndex * 2 + tarjetaIndex;

                        if (posicion >= JUEGOS.length) {
                            continue;
                        }

                        boolean coincide =
                                JUEGOS[posicion]
                                        .toLowerCase()
                                        .contains(texto)
                                        || GENEROS[posicion]
                                        .toLowerCase()
                                        .contains(texto);

                        tarjeta.setVisibility(
                                coincide
                                        ? View.VISIBLE
                                        : View.GONE
                        );
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}