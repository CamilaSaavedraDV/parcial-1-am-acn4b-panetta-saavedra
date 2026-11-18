package com.example.astra;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class CarritoDialog {

    public static String formatearPrecio(double valor) {
        return String.format(Locale.US, "$ %.2f", valor);
    }

    public static void actualizarBadge(Activity activity) {

        TextView badge = activity.findViewById(R.id.tvBadgeCarrito);

        if (badge == null) {
            return;
        }

        int cantidad = Carrito.cantidad();

        badge.setText(String.valueOf(cantidad));
        badge.setVisibility(cantidad > 0 ? View.VISIBLE : View.GONE);
    }

    public static void mostrar(Activity activity) {

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_carrito);

        LinearLayout lista = dialog.findViewById(R.id.contenedorCarrito);
        TextView vacio = dialog.findViewById(R.id.tvCarritoVacio);
        TextView total = dialog.findViewById(R.id.tvTotalCarrito);
        TextView cerrar = dialog.findViewById(R.id.btnCerrarCarrito);

        cerrar.setOnClickListener(v -> dialog.dismiss());

        refrescar(activity, lista, vacio, total);

        dialog.show();

        Window window = dialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            int ancho = (int) (activity.getResources()
                    .getDisplayMetrics().widthPixels * 0.88);

            window.setLayout(ancho, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private static void refrescar(
            Activity activity,
            LinearLayout lista,
            TextView vacio,
            TextView totalView
    ) {

        lista.removeAllViews();

        for (Juego juego : Carrito.getJuegos()) {

            View item = LayoutInflater.from(activity)
                    .inflate(R.layout.item_carrito_juego, lista, false);

            TextView nombre = item.findViewById(R.id.tvNombreCarrito);
            TextView precio = item.findViewById(R.id.tvPrecioCarrito);
            ImageView eliminar = item.findViewById(R.id.btnEliminarJuego);

            nombre.setText(juego.getNombre());
            precio.setText(formatearPrecio(juego.getPrecio()));

            eliminar.setOnClickListener(v -> {
                Carrito.quitar(juego);
                actualizarBadge(activity);
                refrescar(activity, lista, vacio, totalView);

                Toast.makeText(
                        activity,
                        activity.getString(R.string.juego_eliminado, juego.getNombre()),
                        Toast.LENGTH_SHORT
                ).show();
            });

            lista.addView(item);
        }

        vacio.setVisibility(Carrito.cantidad() == 0 ? View.VISIBLE : View.GONE);
        totalView.setText(formatearPrecio(Carrito.total()));
    }
}
