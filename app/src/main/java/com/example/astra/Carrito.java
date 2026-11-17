package com.example.astra;

import java.util.ArrayList;

public class Carrito {

    private static final ArrayList<Juego> juegos = new ArrayList<>();

    public static boolean contiene(Juego juego) {
        for (Juego j : juegos) {
            if (j.getNombre().equals(juego.getNombre())) {
                return true;
            }
        }
        return false;
    }

    // Devuelve false si el juego ya estaba en el carrito
    public static boolean agregar(Juego juego) {
        if (contiene(juego)) {
            return false;
        }
        juegos.add(juego);
        return true;
    }

    public static void quitar(Juego juego) {
        for (int i = 0; i < juegos.size(); i++) {
            if (juegos.get(i).getNombre().equals(juego.getNombre())) {
                juegos.remove(i);
                return;
            }
        }
    }

    public static ArrayList<Juego> getJuegos() {
        return juegos;
    }

    public static int cantidad() {
        return juegos.size();
    }

    public static double total() {
        double total = 0;
        for (Juego j : juegos) {
            total += j.getPrecio();
        }
        return total;
    }
}