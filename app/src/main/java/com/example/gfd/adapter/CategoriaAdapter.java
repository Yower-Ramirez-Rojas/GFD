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
    private double sueldoBase = 0;
    private OnItemLongClickListener listener; // Nuestro nuevo escuchador

    // Interfaz para avisarle al MainActivity qué tarjeta presionaste
    public interface OnItemLongClickListener {
        void onItemLongClick(Categoria categoria);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria, parent, false);
        return new CategoriaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaHolder holder, int position) {
        Categoria categoriaActual = listaCategorias.get(position);
        holder.tvNombre.setText(categoriaActual.nombre);
        holder.tvMonto.setText(String.format("$%,.0f", categoriaActual.montoAsignado));

        if (sueldoBase > 0) {
            double porcentaje = (categoriaActual.montoAsignado / sueldoBase) * 100;
            holder.tvPorcentaje.setText(String.format("%.1f%%", porcentaje));
        } else {
            holder.tvPorcentaje.setText("0%");
        }
    }

    @Override
    public int getItemCount() {
        if (listaCategorias == null) return 0;
        return listaCategorias.size();
    }

    public void setCategorias(List<Categoria> categorias) {
        if (categorias != null) {
            this.listaCategorias = categorias;
        } else {
            this.listaCategorias = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void setSueldoBase(double sueldoBase) {
        this.sueldoBase = sueldoBase;
        notifyDataSetChanged();
    }

    class CategoriaHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre;
        private TextView tvMonto;
        private TextView tvPorcentaje;

        public CategoriaHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCategoria);
            tvMonto = itemView.findViewById(R.id.tvMontoCategoria);
            tvPorcentaje = itemView.findViewById(R.id.tvPorcentaje);

            // ¡Aquí detectamos que mantuviste el dedo presionado!
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(listaCategorias.get(position));
                    return true; // true significa "yo me encargo de este clic"
                }
                return false;
            });
        }
    }
}