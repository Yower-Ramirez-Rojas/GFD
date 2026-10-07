package com.example.gfd.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.gfd.database.AppDatabase;
import com.example.gfd.database.CategoriaDao;
import com.example.gfd.model.Categoria;

public class CategoriaRepository {

    private CategoriaDao categoriaDao;
    private ExecutorService executorService;

    public CategoriaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        categoriaDao = db.categoriaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // ¡NUEVO! Ahora le pasamos el ID de la carpeta para traer solo sus apartados
    public LiveData<List<Categoria>> getCategoriasPorPresupuesto(int presupuestoId) {
        return categoriaDao.obtenerCategoriasPorPresupuesto(presupuestoId);
    }

    // ¡NUEVO! Calcula el total gastado, pero solo de esa carpeta
    public LiveData<Double> getSaldoTotalPorPresupuesto(int presupuestoId) {
        return categoriaDao.obtenerSaldoTotalPorPresupuesto(presupuestoId);
    }

    public void insertar(Categoria categoria) {
        executorService.execute(() -> categoriaDao.insertar(categoria));
    }

    public void actualizar(Categoria categoria) {
        executorService.execute(() -> categoriaDao.actualizar(categoria));
    }

    public void eliminar(Categoria categoria) {
        executorService.execute(() -> categoriaDao.eliminar(categoria));
    }
}