package com.example.gfd.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_categorias")
public class Categoria {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre; // Ej: "Ahorro", "Salidas", "Comida"
    public double montoAsignado;
    public boolean esReservaIntocable; // Para saber si es un "sobre" cerrado

    // Constructor vacío requerido por Room
    public Categoria() {
    }

    // Constructor para cuando creemos una categoría nueva
    public Categoria(String nombre, double montoAsignado, boolean esReservaIntocable) {
        this.nombre = nombre;
        this.montoAsignado = montoAsignado;
        this.esReservaIntocable = esReservaIntocable;
    }
}