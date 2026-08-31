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
    private LiveData<List<Categoria>> todasLasCategorias;
    private LiveData<Double> saldoTotal;

    // Herramienta para ejecutar tareas en segundo plano sin trabar la app
    private ExecutorService executorService;

    public CategoriaRepository(Application application) {
        // Conectamos con la base de datos
        AppDatabase db = AppDatabase.getDatabase(application);
        categoriaDao = db.categoriaDao();

        // Cargamos los datos en vivo
        todasLasCategorias = categoriaDao.obtenerTodasLasCategorias();
        saldoTotal = categoriaDao.obtenerSaldoTotalAsignado();

        // Creamos un hilo de trabajo
        executorService = Executors.newSingleThreadExecutor();
    }

    // --- MÉTODOS PARA LEER DATOS ---
    public LiveData<List<Categoria>> getTodasLasCategorias() {
        return todasLasCategorias;
    }

    public LiveData<Double> getSaldoTotal() {
        return saldoTotal;
    }

    // --- MÉTODOS PARA ESCRIBIR DATOS (En segundo plano) ---
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