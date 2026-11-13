package com.example.gfd.view;

import android.content.Intent;
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

    private int presupuestoId = -1;
    private double sueldoBaseActual = 0;
    private double totalAsignadoActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Recibimos los datos del presupuesto
        if (getIntent() != null) {
            presupuestoId = getIntent().getIntExtra("PRESUPUESTO_ID", -1);
            sueldoBaseActual = getIntent().getDoubleExtra("SUELDO_BASE", 0);
        }

        // 2. Conectamos la interfaz
        tvSaldoDisponible = findViewById(R.id.tvSaldoDisponible);
        tvSueldoTotal = findViewById(R.id.tvSueldoTotal);
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregarCategoria);
        ImageView btnEditarSueldo = findViewById(R.id.btnEditarSueldo);
        ImageView btnCompartir = findViewById(R.id.btnCompartir);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategorias);

        // 3. Asignamos los clics a los botones superiores
        btnCompartir.setOnClickListener(v -> compartirReporte());
        btnEditarSueldo.setOnClickListener(v -> mostrarDialogoEditarSueldo());

        // 4. Configuramos el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new CategoriaAdapter();
        adapter.setSueldoBase(sueldoBaseActual);

        adapter.setOnItemLongClickListener(categoria -> {
            mostrarDialogoEliminar(categoria);
        });

        recyclerView.setAdapter(adapter);

        // 5. Conectamos con la Base de Datos (ViewModel)
        categoriaViewModel = new ViewModelProvider(this).get(CategoriaViewModel.class);

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
            finish();
        }

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
                        Categoria nuevaCategoria = new Categoria(presupuestoId, nombre, monto, false);
                        categoriaViewModel.insertar(nuevaCategoria);
                    } else {
                        Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEditarSueldo() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_editar_presupuesto, null);
        EditText etNuevoSueldo = view.findViewById(R.id.etNuevoSueldo);

        // Autocompletamos con el sueldo actual para facilitar la edición
        etNuevoSueldo.setText(String.valueOf(sueldoBaseActual));

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Actualizar", (dialog, which) -> {
                    String montoStr = etNuevoSueldo.getText().toString().trim();

                    if (!montoStr.isEmpty()) {
                        double nuevoSueldo = Double.parseDouble(montoStr);
                        sueldoBaseActual = nuevoSueldo;
                        actualizarUI();
                        Toast.makeText(this, "Sueldo actualizado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Por favor, ingresa un monto", Toast.LENGTH_SHORT).show();
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

    private void compartirReporte() {
        double disponible = sueldoBaseActual - totalAsignadoActual;

        String reporte = "📊 *REPORTE FINANCIERO GFD*\n\n" +
                "💰 Sueldo Inicial: $" + String.format("%,.0f", sueldoBaseActual) + "\n" +
                "📉 Total Gastos: $" + String.format("%,.0f", totalAsignadoActual) + "\n" +
                "💵 Saldo Disponible: $" + String.format("%,.0f", disponible) + "\n\n" +
                "Seguridad Biométrica Activa 🔒\n" +
                "Generado desde mi Bóveda GFD.";

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, reporte);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Compartir reporte financiero vía...");
        startActivity(shareIntent);
    }
}