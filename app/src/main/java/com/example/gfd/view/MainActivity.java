package com.example.gfd.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.gfd.R;
import com.example.gfd.adapter.CategoriaAdapter;
import com.example.gfd.model.Categoria;
import com.example.gfd.viewmodel.CategoriaViewModel;

public class MainActivity extends AppCompatActivity {

    private CategoriaViewModel categoriaViewModel;
    private TextView tvSaldoTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSaldoTotal = findViewById(R.id.tvSaldoTotalMain);
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregar);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategorias);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        CategoriaAdapter adapter = new CategoriaAdapter();
        recyclerView.setAdapter(adapter);

        categoriaViewModel = new ViewModelProvider(this).get(CategoriaViewModel.class);

        categoriaViewModel.getSaldoTotal().observe(this, saldo -> {
            if (saldo != null) {
                tvSaldoTotal.setText(String.format("$%.0f", saldo));
            } else {
                tvSaldoTotal.setText("$0");
            }
        });

        categoriaViewModel.getTodasLasCategorias().observe(this, categorias -> {
            adapter.setCategorias(categorias);
        });

        // Aquí conectamos el clic del botón con la ventana emergente
        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregar());
    }

    // Método que crea y muestra la ventanita
    private void mostrarDialogoAgregar() {
        // Traemos el diseño XML que creaste recién
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_categoria, null);
        EditText etNombre = view.findViewById(R.id.etNombreCategoria);
        EditText etMonto = view.findViewById(R.id.etMontoCategoria);

        // Construimos la ventana
        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String montoStr = etMonto.getText().toString().trim();

                    // Verificamos que el usuario haya escrito algo
                    if (!nombre.isEmpty() && !montoStr.isEmpty()) {
                        double monto = Double.parseDouble(montoStr);

                        // ¡Aquí ocurre la magia! Creamos el apartado y lo guardamos
                        Categoria nuevaCategoria = new Categoria(nombre, monto, false);
                        categoriaViewModel.insertar(nuevaCategoria);

                    } else {
                        Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}