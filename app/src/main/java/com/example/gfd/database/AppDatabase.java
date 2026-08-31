package com.example.gfd.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.gfd.model.Categoria;

// Aquí le decimos a Room qué tablas existen y qué versión de base de datos es
@Database(entities = {Categoria.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Le decimos que use el DAO que creamos antes
    public abstract CategoriaDao categoriaDao();

    // Variable para guardar la instancia única de la base de datos
    private static volatile AppDatabase INSTANCIA;

    // Método para obtener la base de datos (Patrón Singleton)
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCIA == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "gfd_database")
                            .build();
                }
            }
        }
        return INSTANCIA;
    }
}