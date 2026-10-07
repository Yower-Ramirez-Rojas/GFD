package com.example.gfd.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gfd.model.Categoria;
import com.example.gfd.model.Presupuesto; // Importamos el nuevo modelo

// ¡AQUÍ agregamos la nueva clase y subimos la versión a 2 por seguridad!
@Database(entities = {Categoria.class, Presupuesto.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoriaDao categoriaDao();
    public abstract PresupuestoDao presupuestoDao(); // Conectamos el nuevo control remoto

    private static volatile AppDatabase INSTANCIA;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCIA == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "gfd_database")
                            // Este seguro de vida evita que la app choque al cambiar las tablas
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCIA;
    }
}