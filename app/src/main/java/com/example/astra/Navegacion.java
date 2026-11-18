package com.example.astra;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class Navegacion {

    public static final int NINGUNO = -1;
    public static final int INICIO = 0;
    public static final int TIENDA = 1;
    public static final int BIBLIOTECA = 2;

    public static void configurar(Activity activity, int actual) {
        configurarEncabezado(activity);
        configurarBarra(activity, actual);
    }

    private static void configurarEncabezado(Activity activity) {

        ImageView btnNotificaciones = activity.findViewById(R.id.btnNotificaciones);
        ImageView btnCarrito = activity.findViewById(R.id.btnCarrito);
        ImageView btnPerfil = activity.findViewById(R.id.btnPerfil);

        btnNotificaciones.setOnClickListener(v ->
                Toast.makeText(activity, R.string.notificaciones_toast, Toast.LENGTH_SHORT).show()
        );

        btnCarrito.setOnClickListener(v -> CarritoDialog.mostrar(activity));

        btnPerfil.setOnClickListener(v -> {
            if (!(activity instanceof PerfilActivity)) {
                irA(activity, PerfilActivity.class);
            }
        });

        CarritoDialog.actualizarBadge(activity);
    }

    private static void configurarBarra(Activity activity, int actual) {

        LinearLayout barra = activity.findViewById(R.id.barraNavegacion);

        for (int i = 0; i < 3; i++) {

            LinearLayout item = (LinearLayout) barra.getChildAt(i);
            ImageView icono = (ImageView) item.getChildAt(0);
            TextView texto = (TextView) item.getChildAt(1);

            int color = ContextCompat.getColor(
                    activity,
                    i == actual ? R.color.acento : R.color.texto_principal
            );

            icono.setImageTintList(ColorStateList.valueOf(color));
            texto.setTextColor(color);

            final int destino = i;

            item.setOnClickListener(v -> {
                if (destino != actual) {
                    navegar(activity, destino);
                }
            });
        }
    }

    private static void navegar(Activity activity, int destino) {

        switch (destino) {
            case INICIO:
                irA(activity, MainActivity.class);
                break;
            case TIENDA:
                irA(activity, TiendaActivity.class);
                break;
            case BIBLIOTECA:
                // TODO (Camila): reemplazar por la BibliotecaActivity cuando exista
                Toast.makeText(activity, R.string.proximamente, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private static void irA(Activity activity, Class<?> destino) {
        Intent intent = new Intent(activity, destino);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }
}
