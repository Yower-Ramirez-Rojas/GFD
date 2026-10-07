package com.example.gfd.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.gfd.R;
import com.example.gfd.adapter.PresupuestoAdapter;
import com.example.gfd.model.Presupuesto;
import com.example.gfd.viewmodel.PresupuestoViewModel;

public class MenuPrincipalActivity extends AppCompatActivity {

    private PresupuestoViewModel presupuestoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        RecyclerView recyclerView = findViewById(R.id.rvPresupuestos);
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregarPresupuesto);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        PresupuestoAdapter adapter = new PresupuestoAdapter();
        recyclerView.setAdapter(adapter);

        presupuestoViewModel = new ViewModelProvider(this).get(PresupuestoViewModel.class);
        presupuestoViewModel.getTodosLosPresupuestos().observe(this, presupuestos -> {
            adapter.setPresupuestos(presupuestos);
        });

        // Al hacer clic normal, viajamos a la MainActivity y le mandamos el ID de la carpeta
        adapter.setOnItemClickListener(presupuesto -> {
            Intent intent = new Intent(MenuPrincipalActivity.this, MainActivity.class);
            intent.putExtra("PRESUPUESTO_ID", presupuesto.id);
            intent.putExtra("SUELDO_BASE", presupuesto.sueldoBase);
            startActivity(intent);
        });

        // Al mantener presionado, borramos la carpeta
        adapter.setOnItemLongClickListener(presupuesto -> {
            mostrarDialogoEliminar(presupuesto);
        });

        // Botón "+" para crear nueva carpeta
        fabAgregar.setOnClickListener(v -> mostrarDialogoCrearPresupuesto());
    }

    private void mostrarDialogoCrearPresupuesto() {
        // Armamos el diseño de la ventanita desde código (sin XML extra)
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        EditText etNombre = new EditText(this);
        etNombre.setHint("Ej: Octubre 2026, Viaje...");
        layout.addView(etNombre);

        EditText etSueldo = new EditText(this);
        etSueldo.setHint("Sueldo / Dinero Inicial");
        etSueldo.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etSueldo);

        new AlertDialog.Builder(this)
                .setTitle("Nueva Sesión")
                .setView(layout)
                .setPositiveButton("Crear", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String sueldoStr = etSueldo.getText().toString().trim();

                    if (!nombre.isEmpty() && !sueldoStr.isEmpty()) {
                        double sueldo = Double.parseDouble(sueldoStr);
                        Presupuesto nuevoPresupuesto = new Presupuesto(nombre, sueldo);
                        presupuestoViewModel.insertar(nuevoPresupuesto);
                    } else {
                        Toast.makeText(this, "Por favor llena ambos datos", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEliminar(Presupuesto presupuesto) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Sesión")
                .setMessage("¿Seguro que quieres borrar '" + presupuesto.nombre + "'? Se eliminarán todos los apartados de esta carpeta para siempre.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    presupuestoViewModel.eliminar(presupuesto);
                    Toast.makeText(this, "Sesión eliminada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}