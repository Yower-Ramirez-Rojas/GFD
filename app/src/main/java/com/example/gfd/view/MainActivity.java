package com.example.gfd.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
    private TextView tvSaldoDisponible;
    private TextView tvSueldoTotal;
    private CategoriaAdapter adapter;

    // ¡NUEVO! Aquí guardaremos el ID de la carpeta que tocaste en el menú
    private int presupuestoId = -1;
    private double sueldoBaseActual = 0;
    private double totalAsignadoActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Recibimos los datos de la carpeta que nos mandó el Menú
        if (getIntent() != null) {
            presupuestoId = getIntent().getIntExtra("PRESUPUESTO_ID", -1);
            sueldoBaseActual = getIntent().getDoubleExtra("SUELDO_BASE", 0);
        }

        // 2. Conectamos los IDs
        tvSaldoDisponible = findViewById(R.id.tvSaldoDisponible);
        tvSueldoTotal = findViewById(R.id.tvSueldoTotal);
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregarCategoria);
        ImageView btnEditarSueldo = findViewById(R.id.btnEditarSueldo);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategorias);

        // Ocultamos el botón de editar sueldo porque ahora el sueldo se define al crear la carpeta
        btnEditarSueldo.setVisibility(View.GONE);

        // 3. Configuramos la lista
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new CategoriaAdapter();
        adapter.setSueldoBase(sueldoBaseActual);

        adapter.setOnItemLongClickListener(categoria -> {
            mostrarDialogoEliminar(categoria);
        });

        recyclerView.setAdapter(adapter);

        // 4. Conectamos la base de datos
        categoriaViewModel = new ViewModelProvider(this).get(CategoriaViewModel.class);

        // ¡NUEVO! Observamos solo los datos de esta carpeta en específico
        if (presupuestoId != -1) {
            categoriaViewModel.getSaldoTotalPorPresupuesto(presupuestoId).observe(this, saldo -> {
                totalAsignadoActual = (saldo != null) ? saldo : 0;
                actualizarUI();
            });

            categoriaViewModel.getCategoriasPorPresupuesto(presupuestoId).observe(this, categorias -> {
                adapter.setCategorias(categorias);
            });
        } else {
            Toast.makeText(this, "Error al cargar la sesión", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la pantalla si hubo un error al cargar
        }

        // 5. Botón agregar
        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        actualizarUI();
    }

    private void actualizarUI() {
        double disponible = sueldoBaseActual - totalAsignadoActual;
        tvSueldoTotal.setText(String.format("$%,.0f", sueldoBaseActual));
        tvSaldoDisponible.setText(String.format("$%,.0f", disponible));
        if (adapter != null) {
            adapter.setSueldoBase(sueldoBaseActual);
        }
    }

    private void mostrarDialogoAgregar() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_categoria, null);
        EditText etNombre = view.findViewById(R.id.etNombreCategoria);
        EditText etMonto = view.findViewById(R.id.etMontoCategoria);

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String montoStr = etMonto.getText().toString().trim();

                    if (!nombre.isEmpty() && !montoStr.isEmpty()) {
                        double monto = Double.parseDouble(montoStr);
                        // ¡NUEVO! Le decimos a la base de datos a qué carpeta pertenece este gasto
                        Categoria nuevaCategoria = new Categoria(presupuestoId, nombre, monto, false);
                        categoriaViewModel.insertar(nuevaCategoria);
                    } else {
                        Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEliminar(Categoria categoria) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar apartado")
                .setMessage("¿Estás seguro de que quieres eliminar '" + categoria.nombre + "'? Ese dinero volverá a tu saldo disponible.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    categoriaViewModel.eliminar(categoria);
                    Toast.makeText(this, "Apartado eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}