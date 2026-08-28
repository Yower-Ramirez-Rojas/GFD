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

    // 1. Guardar una nueva categoría o reserva
    @Insert
    void insertar(Categoria categoria);

    // 2. Modificar una categoría existente (ej. cambiarle el nombre o monto)
    @Update
    void actualizar(Categoria categoria);

    // 3. Borrar una categoría
    @Delete
    void eliminar(Categoria categoria);

    // 4. Leer todas las categorías creadas
    // Usamos LiveData para que la pantalla se actualice sola si el dinero cambia
    @Query("SELECT * FROM tabla_categorias")
    LiveData<List<Categoria>> obtenerTodasLasCategorias();

    // 5. Un plus para tu gestor: Calcular todo el dinero que tienes repartido
    @Query("SELECT SUM(montoAsignado) FROM tabla_categorias")
    LiveData<Double> obtenerSaldoTotalAsignado();
}