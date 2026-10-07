package com.example.gfd.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// Le decimos a Room que esta tabla está amarrada a la tabla de presupuestos
@Entity(tableName = "tabla_categorias",
        foreignKeys = @ForeignKey(entity = Presupuesto.class,
                parentColumns = "id",
                childColumns = "presupuestoId",
                onDelete = ForeignKey.CASCADE)) // CASCADE: Si borras "Octubre", se borran todos sus gastos
public class Categoria {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int presupuestoId; // <--- ESTE ES EL GANCHO HACIA LA CARPETA PADRE

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