package com.example.astra;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

public class Dinamico {

    private static final String[] JUEGOS = {
            "Hollow Knight", "Minecraft", "Stardew Valley", "Hades", "Celeste",
            "Terraria", "Dead Cells", "Cuphead", "Undertale", "Among Us"};
    private static final double[] PRECIOS = {
            14.99, 24.99, 19.99, 24.99, 19.99,
            9.99, 24.99, 19.99, 9.99, 4.99};

    public static void iniciar(Activity activity) {
        NestedScrollView scroll = activity.findViewById(R.id.scrollPrincipal);
        LinearLayout contenido = (LinearLayout) scroll.getChildAt(0);
        int margen = activity.getResources().getDimensionPixelSize(R.dimen.margen_item);

        Button btnAgregar = crearBoton(activity, R.string.boton_agregar);
        Button btnVaciar = crearBoton(activity, R.string.boton_vaciar);
        TextView tvTotal = crearTexto(activity, margen);
        TextView tvVacio = crearTexto(activity, margen);
        LinearLayout carrito = new LinearLayout(activity);
        carrito.setOrientation(LinearLayout.VERTICAL);

        tvVacio.setText(R.string.carrito_vacio);

        contenido.addView(btnAgregar);
        contenido.addView(btnVaciar);
        contenido.addView(tvTotal);
        contenido.addView(tvVacio);
        contenido.addView(carrito);

        int[] cantidad = {0};
        double[] total = {0};

        Runnable actualizar = () -> {
            if (carrito.getChildCount() == 0) {
                total[0] = 0;
            }
            tvVacio.setVisibility(carrito.getChildCount() == 0 ? View.VISIBLE : View.GONE);
            tvTotal.setText(activity.getString(R.string.carrito_total, total[0]));
        };
        actualizar.run();

        btnAgregar.setOnClickListener(v -> {
            int i = cantidad[0] % JUEGOS.length;
            cantidad[0]++;
            double precio = PRECIOS[i];
            total[0] += precio;

            View tarjeta = LayoutInflater.from(activity)
                    .inflate(R.layout.item_carrito, carrito, false);
            ((TextView) tarjeta.findViewById(R.id.tvNombre)).setText(JUEGOS[i]);
            ((TextView) tarjeta.findViewById(R.id.tvPrecio))
                    .setText(activity.getString(R.string.precio_juego, precio));

            tarjeta.findViewById(R.id.btnQuitar).setOnClickListener(b -> {
                carrito.removeView(tarjeta);
                total[0] -= precio;
                actualizar.run();
            });

            carrito.addView(tarjeta);
            actualizar.run();
        });

        btnVaciar.setOnClickListener(v -> {
            carrito.removeAllViews();
            cantidad[0] = 0;
            actualizar.run();
        });
    }

    private static Button crearBoton(Activity activity, int textoRes) {
        Button boton = new Button(activity);
        boton.setText(textoRes);
        boton.setTextColor(ContextCompat.getColor(activity, R.color.texto_boton));
        boton.setBackgroundColor(ContextCompat.getColor(activity, R.color.acento_boton));
        return boton;
    }

    private static TextView crearTexto(Activity activity, int margen) {
        TextView texto = new TextView(activity);
        texto.setTextColor(ContextCompat.getColor(activity, R.color.texto_principal));
        texto.setPadding(0, margen, 0, margen);
        return texto;
    }
}