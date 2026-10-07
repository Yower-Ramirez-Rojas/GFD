package com.example.gfd.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import com.example.gfd.model.Categoria;
import com.example.gfd.repository.CategoriaRepository;

public class CategoriaViewModel extends AndroidViewModel {

    private CategoriaRepository repository;

    public CategoriaViewModel(@NonNull Application application) {
        super(application);
        repository = new CategoriaRepository(application);
    }

    // Pasamos el ID hacia el repositorio
    public LiveData<List<Categoria>> getCategoriasPorPresupuesto(int presupuestoId) {
        return repository.getCategoriasPorPresupuesto(presupuestoId);
    }

    // Pasamos el ID hacia el repositorio
    public LiveData<Double> getSaldoTotalPorPresupuesto(int presupuestoId) {
        return repository.getSaldoTotalPorPresupuesto(presupuestoId);
    }

    public void insertar(Categoria categoria) {
        repository.insertar(categoria);
    }

    public void actualizar(Categoria categoria) {
        repository.actualizar(categoria);
    }

    public void eliminar(Categoria categoria) {
        repository.eliminar(categoria);
    }
}