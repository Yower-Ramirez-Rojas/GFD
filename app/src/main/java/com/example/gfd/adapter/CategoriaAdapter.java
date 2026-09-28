package com.example.gfd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gfd.R;
import com.example.gfd.model.Categoria;
import java.util.ArrayList;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaHolder> {

    private List<Categoria> listaCategorias = new ArrayList<>();

    @NonNull
    @Override
    public CategoriaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Aquí conectamos el molde XML que acabas de crear
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria, parent, false);
        return new CategoriaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaHolder holder, int position) {
        // Aquí tomamos cada categoría y escribimos sus datos en la tarjeta
        Categoria categoriaActual = listaCategorias.get(position);
        holder.tvNombre.setText(categoriaActual.nombre);
        // Formateamos el texto para que se vea como dinero
        holder.tvMonto.setText(String.format("$%.0f", categoriaActual.montoAsignado));
    }

    @Override
    public int getItemCount() {
        // Seguro de vida 1: Si por alguna razón llega nulo, decimos que hay 0 tarjetas
        if (listaCategorias == null) {
            return 0;
        }
        return listaCategorias.size();
    }

    public void setCategorias(List<Categoria> categorias) {
        // Seguro de vida 2: Solo actualizamos la lista si Room nos envía datos reales
        if (categorias != null) {
            this.listaCategorias = categorias;
        } else {
            this.listaCategorias = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    // Clase interna que enlaza los textos del XML con el código
    class CategoriaHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre;
        private TextView tvMonto;

        public CategoriaHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCategoria);
            tvMonto = itemView.findViewById(R.id.tvMontoCategoria);
        }
    }
}