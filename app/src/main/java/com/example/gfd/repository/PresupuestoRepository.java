package com.example.gfd.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.gfd.database.AppDatabase;
import com.example.gfd.database.PresupuestoDao;
import com.example.gfd.model.Presupuesto;

public class PresupuestoRepository {

    private PresupuestoDao presupuestoDao;
    private LiveData<List<Presupuesto>> todosLosPresupuestos;
    private ExecutorService executorService;

    public PresupuestoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        presupuestoDao = db.presupuestoDao();
        todosLosPresupuestos = presupuestoDao.obtenerTodosLosPresupuestos();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Presupuesto>> getTodosLosPresupuestos() {
        return todosLosPresupuestos;
    }

    public void insertar(Presupuesto presupuesto) {
        executorService.execute(() -> presupuestoDao.insertar(presupuesto));
    }

    public void eliminar(Presupuesto presupuesto) {
        executorService.execute(() -> presupuestoDao.eliminar(presupuesto));
    }
}