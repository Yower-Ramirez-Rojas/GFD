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
    private LiveData<List<Categoria>> todasLasCategorias;
    private LiveData<Double> saldoTotal;

    public CategoriaViewModel(@NonNull Application application) {
        super(application);
        // Conectamos con el repositorio que acabas de crear
        repository = new CategoriaRepository(application);
        todasLasCategorias = repository.getTodasLasCategorias();
        saldoTotal = repository.getSaldoTotal();
    }

    // --- MÉTODOS PARA QUE LA PANTALLA LEA LOS DATOS ---
    public LiveData<List<Categoria>> getTodasLasCategorias() {
        return todasLasCategorias;
    }

    public LiveData<Double> getSaldoTotal() {
        return saldoTotal;
    }

    // --- MÉTODOS PARA QUE LA PANTALLA ENvíE ÓRDENES DE GUARDAR/BORRAR ---
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
