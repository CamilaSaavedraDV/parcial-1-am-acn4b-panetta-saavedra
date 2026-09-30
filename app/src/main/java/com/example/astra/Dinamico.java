package com.example.astra;

import android.app.Activity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;

public class Dinamico {

    public static void iniciar(Activity activity) {
        NestedScrollView scroll = activity.findViewById(R.id.scrollPrincipal);
        LinearLayout contenido = (LinearLayout) scroll.getChildAt(0);

        Button btnAgregar = new Button(activity);
        btnAgregar.setText("Agregar juego al carrito");
        contenido.addView(btnAgregar);

        LinearLayout carrito = new LinearLayout(activity);
        carrito.setOrientation(LinearLayout.VERTICAL);
        contenido.addView(carrito);

        btnAgregar.setOnClickListener(v -> {
            TextView item = new TextView(activity);
            item.setText("Hollow Knight - $9.99");
            item.setTextColor(0xFFFFFFFF);
            carrito.addView(item);
        });
    }
}
