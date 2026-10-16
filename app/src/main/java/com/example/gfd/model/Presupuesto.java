package com.example.gfd.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_presupuestos")
public class Presupuesto {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public double sueldoBase;


    public Presupuesto() {
    }

    public Presupuesto(String nombre, double sueldoBase) {
        this.nombre = nombre;
        this.sueldoBase = sueldoBase;
    }
}