package com.example.gfd.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "tabla_categorias",
        foreignKeys = @ForeignKey(entity = Presupuesto.class,
                parentColumns = "id",
                childColumns = "presupuestoId",
                onDelete = ForeignKey.CASCADE))
public class Categoria {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int presupuestoId;

    public String nombre;
    public double montoAsignado;
    public boolean esReservaIntocable;

    public Categoria() {
    }

    public Categoria(int presupuestoId, String nombre, double montoAsignado, boolean esReservaIntocable) {
        this.presupuestoId = presupuestoId;
        this.nombre = nombre;
        this.montoAsignado = montoAsignado;
        this.esReservaIntocable = esReservaIntocable;
    }
}