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

    private static final Juego[] JUEGOS = {
            new Juego("Hollow Knight", "Metroidvania", 8.99, "hollow"),
            new Juego("Stardew Valley", "Simulación", 11.24, "stardew_valley"),
            new Juego("Call of Duty", "Acción | FPS", 9.89, "callofdutty"),
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
        ImageView btnNotificaciones = activity.findViewById(R.id.btnNotificaciones);
        ImageView btnCarrito = activity.findViewById(R.id.btnCarrito);
        CarritoDialog.actualizarBadge(activity);
        cargarOfertas(activity, contenedor);
        configurarBuscador(buscador, contenedor);

        verTodos.setOnClickListener(v -> {
            buscador.setText("");
            Toast.makeText(activity, "Mostrando todos los juegos", Toast.LENGTH_SHORT).show();
        });

        btnNotificaciones.setOnClickListener(v -> {
            Toast.makeText(activity, "Notificaciones", Toast.LENGTH_SHORT).show();
        });

        btnCarrito.setOnClickListener(v -> CarritoDialog.mostrar(activity));
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

            Juego juego = JUEGOS[i];
            imagen.setImageResource(juego.getImagenRes(activity));
            nombre.setText(juego.getNombre());
            genero.setText(juego.getGenero());
            descuento.setText(DESCUENTOS[i]);
            precio.setText(juego.getPrecioTexto());

            final int posicion = i;

            agregar.setOnClickListener(v -> {
                Juego juegoSeleccionado = JUEGOS[posicion];
                if (!Carrito.agregar(juegoSeleccionado)) {
                    Toast.makeText(
                            activity,
                            activity.getString(R.string.juego_ya_en_carrito, juegoSeleccionado.getNombre()),
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                CarritoDialog.actualizarBadge(activity);

                Toast.makeText(
                        activity,
                        activity.getString(R.string.juego_agregado, juegoSeleccionado.getNombre()),
                        Toast.LENGTH_SHORT
                ).show();
            });

            tarjeta.setOnClickListener(v ->
                    Toast.makeText(
                            activity,
                            juego.getNombre(),
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
                                JUEGOS[posicion].getNombre()
                                        .toLowerCase()
                                        .contains(texto)
                                        || JUEGOS[posicion].getGenero()
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
