package com.example.gfd.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.gfd.model.Categoria;
import java.util.List;

@Dao
public interface CategoriaDao {
    @Insert
    void insertar(Categoria categoria);

    @Update
    void actualizar(Categoria categoria);

    @Delete
    void eliminar(Categoria categoria);


    @Query("SELECT * FROM tabla_categorias WHERE presupuestoId = :presupuestoId")
    LiveData<List<Categoria>> obtenerCategoriasPorPresupuesto(int presupuestoId);


    @Query("SELECT SUM(montoAsignado) FROM tabla_categorias WHERE presupuestoId = :presupuestoId")
    LiveData<Double> obtenerSaldoTotalPorPresupuesto(int presupuestoId);
}