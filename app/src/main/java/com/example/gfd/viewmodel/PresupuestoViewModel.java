package com.example.gfd.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import com.example.gfd.model.Presupuesto;
import com.example.gfd.repository.PresupuestoRepository;

public class PresupuestoViewModel extends AndroidViewModel {

    private PresupuestoRepository repository;
    private LiveData<List<Presupuesto>> todosLosPresupuestos;

    public PresupuestoViewModel(@NonNull Application application) {
        super(application);
        repository = new PresupuestoRepository(application);
        todosLosPresupuestos = repository.getTodosLosPresupuestos();
    }

    public LiveData<List<Presupuesto>> getTodosLosPresupuestos() {
        return todosLosPresupuestos;
    }

    public void insertar(Presupuesto presupuesto) {
        repository.insertar(presupuesto);
    }

    public void eliminar(Presupuesto presupuesto) {
        repository.eliminar(presupuesto);
    }
}