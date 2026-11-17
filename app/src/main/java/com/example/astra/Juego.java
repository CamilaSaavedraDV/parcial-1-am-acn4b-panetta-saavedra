package com.example.astra;

import android.content.Context;

import java.io.Serializable;
import java.util.Locale;

public class Juego implements Serializable {

    private final String nombre;
    private final String genero;
    private final double precio;
    private final String imagen;

    public Juego(String nombre, String genero, double precio, String imagen) {
        this.nombre = nombre;
        this.genero = genero;
        this.precio = precio;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public double getPrecio() {
        return precio;
    }

    public String getPrecioTexto() {
        return String.format(Locale.US, "$ %.2f", precio);
    }

    public int getImagenRes(Context context) {
        int id = context.getResources()
                .getIdentifier(imagen, "drawable", context.getPackageName());
        return id != 0 ? id : R.drawable.estrella_logo;
    }
}