package com.example.astra;

import java.util.ArrayList;

public class Catalogo {

    public static Juego eldenRing() {
        return new Juego("Elden Ring", "RPG | Acción", 59.99, "elden_ring");
    }

    public static ArrayList<Juego> masVendidos() {
        ArrayList<Juego> lista = new ArrayList<>();
        lista.add(new Juego("EA Sports FC 26", "Deportes | Fútbol", 69.99, "ea_fc_26"));
        lista.add(new Juego("Palworld", "Aventura | RPG | Acción", 29.99, "palworld"));
        lista.add(new Juego("Baldur's Gate 3", "RPG | Estrategia", 59.99, "baldurs_gate_3"));
        return lista;
    }

    public static ArrayList<Juego> gratuitos() {
        ArrayList<Juego> lista = new ArrayList<>();
        lista.add(new Juego("Counter-Strike 2", "Acción | FPS", 0, "counter_strike_2"));
        lista.add(new Juego("Dota 2", "MOBA | Estrategia", 0, "dota_2"));
        lista.add(new Juego("Warframe", "Acción | Shooter", 0, "warframe"));
        return lista;
    }
}
