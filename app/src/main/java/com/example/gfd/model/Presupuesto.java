package com.example.gfd.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_presupuestos")
public class Presupuesto {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre; // Ej: "Octubre 2026", "Ahorros Viaje"
    public double sueldoBase; // Cada carpeta tiene su propio dinero inicial

    // Constructor vacío exigido por Room
    public Presupuesto() {
    }

    public Presupuesto(String nombre, double sueldoBase) {
        this.nombre = nombre;
        this.sueldoBase = sueldoBase;
    }
}