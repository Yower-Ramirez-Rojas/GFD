package com.example.gfd.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.gfd.model.Presupuesto;
import java.util.List;

@Dao
public interface PresupuestoDao {
    @Insert
    void insertar(Presupuesto presupuesto);

    @Delete
    void eliminar(Presupuesto presupuesto);


    @Query("SELECT * FROM tabla_presupuestos ORDER BY id DESC")
    LiveData<List<Presupuesto>> obtenerTodosLosPresupuestos();
}