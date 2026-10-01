package com.example.astra;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import java.util.function.IntConsumer;

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

        Resources res = activity.getResources();
        int margenItem = res.getDimensionPixelSize(R.dimen.margen_item);
        int margenSeccion = res.getDimensionPixelSize(R.dimen.margen_seccion);
        int altoBoton = res.getDimensionPixelSize(R.dimen.alto_boton);

        TextView tvTitulo = crearTexto(activity, R.color.texto_principal,
                R.dimen.texto_titulo, true);
        tvTitulo.setText(R.string.titulo_carrito);
        LinearLayout.LayoutParams pTitulo = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pTitulo.topMargin = margenSeccion;
        contenido.addView(tvTitulo, pTitulo);

        LinearLayout fila = new LinearLayout(activity);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams pFila = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pFila.topMargin = margenItem * 2;
        contenido.addView(fila, pFila);

        Button btnAgregar = crearBoton(activity, R.string.boton_agregar,
                R.drawable.bg_boton, R.color.texto_boton);
        Button btnVaciar = crearBoton(activity, R.string.boton_vaciar,
                R.drawable.bg_boton_borde, R.color.acento);
        LinearLayout.LayoutParams pAgregar = new LinearLayout.LayoutParams(0, altoBoton, 1f);
        LinearLayout.LayoutParams pVaciar = new LinearLayout.LayoutParams(0, altoBoton, 1f);
        pVaciar.setMarginStart(margenItem);
        fila.addView(btnAgregar, pAgregar);
        fila.addView(btnVaciar, pVaciar);

        TextView tvTotal = crearTexto(activity, R.color.acento, R.dimen.texto_titulo, true);
        tvTotal.setGravity(Gravity.END);
        LinearLayout.LayoutParams pTotal = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pTotal.topMargin = margenItem * 2;
        contenido.addView(tvTotal, pTotal);

        TextView tvVacio = crearTexto(activity, R.color.texto_secundario,
                R.dimen.texto_item, false);
        tvVacio.setText(R.string.carrito_vacio);
        tvVacio.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams pVacio = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pVacio.topMargin = margenSeccion;
        contenido.addView(tvVacio, pVacio);

        LinearLayout carrito = new LinearLayout(activity);
        carrito.setOrientation(LinearLayout.VERTICAL);
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

        IntConsumer agregar = i -> {
            double precio = PRECIOS[i];
            total[0] += precio;

            View tarjeta = LayoutInflater.from(activity)
                    .inflate(R.layout.item_carrito, carrito, false);
            ((TextView) tarjeta.findViewById(R.id.tvInicial))
                    .setText(JUEGOS[i].substring(0, 1));
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
        };

        btnAgregar.setOnClickListener(v -> {
            agregar.accept(cantidad[0] % JUEGOS.length);
            cantidad[0]++;
        });

        btnVaciar.setOnClickListener(v -> {
            carrito.removeAllViews();
            cantidad[0] = 0;
            actualizar.run();
        });

        configurarPantallaPrincipal(activity, agregar, scroll, tvTitulo);
    }

    private static void configurarPantallaPrincipal(Activity activity, IntConsumer agregar,
                                                    NestedScrollView scroll, View tituloCarrito) {
        LinearLayout ofertas = activity.findViewById(R.id.contenedorOfertas);
        EditText buscador = activity.findViewById(R.id.buscador);
        View verTodos = activity.findViewById(R.id.verTodos);
        View btnCarrito = activity.findViewById(R.id.btnCarrito);

        TextView sinResultados = crearTexto(activity, R.color.texto_secundario,
                R.dimen.texto_item, false);
        sinResultados.setText(R.string.sin_resultados);
        sinResultados.setGravity(Gravity.CENTER);
        sinResultados.setVisibility(View.GONE);
        ofertas.addView(sinResultados, new LinearLayout.LayoutParams(
                activity.getResources().getDimensionPixelSize(R.dimen.ancho_destacado),
                LinearLayout.LayoutParams.MATCH_PARENT));

        int cantidadOfertas = ofertas.getChildCount() - 1;

        for (int k = 0; k < cantidadOfertas; k++) {
            TextView tarjeta = (TextView) ofertas.getChildAt(k);
            String nombre = tarjeta.getText().toString().split("\n")[0];
            tarjeta.setOnClickListener(v -> {
                for (int i = 0; i < JUEGOS.length; i++) {
                    if (JUEGOS[i].equals(nombre)) {
                        agregar.accept(i);
                        Toast.makeText(activity,
                                activity.getString(R.string.juego_agregado, nombre),
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            });
        }

        btnCarrito.setOnClickListener(v -> scroll.smoothScrollTo(0, tituloCarrito.getTop()));

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String texto = s.toString().trim().toLowerCase();
                int visibles = 0;
                for (int k = 0; k < cantidadOfertas; k++) {
                    TextView tarjeta = (TextView) ofertas.getChildAt(k);
                    String nombre = tarjeta.getText().toString().split("\n")[0].toLowerCase();
                    boolean coincide = nombre.contains(texto);
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

        verTodos.setOnClickListener(v -> {
            buscador.setText("");
            Toast.makeText(activity, R.string.ver_todos_mensaje, Toast.LENGTH_SHORT).show();
        });
    }

    private static Button crearBoton(Activity activity, int textoRes,
                                     int fondoRes, int colorTextoRes) {
        Button boton = new Button(activity);
        boton.setText(textoRes);
        boton.setAllCaps(false);
        boton.setTypeface(null, Typeface.BOLD);
        boton.setTextColor(ContextCompat.getColor(activity, colorTextoRes));
        boton.setBackgroundResource(fondoRes);
        return boton;
    }

    private static TextView crearTexto(Activity activity, int colorRes,
                                       int tamanoRes, boolean negrita) {
        TextView texto = new TextView(activity);
        texto.setTextColor(ContextCompat.getColor(activity, colorRes));
        texto.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                activity.getResources().getDimension(tamanoRes));
        if (negrita) {
            texto.setTypeface(null, Typeface.BOLD);
        }
        return texto;
    }
}