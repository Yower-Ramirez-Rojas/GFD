package com.example.gfd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gfd.R;
import com.example.gfd.model.Presupuesto;
import java.util.ArrayList;
import java.util.List;

public class PresupuestoAdapter extends RecyclerView.Adapter<PresupuestoAdapter.PresupuestoHolder> {

    private List<Presupuesto> listaPresupuestos = new ArrayList<>();
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Presupuesto presupuesto);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Presupuesto presupuesto);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public PresupuestoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_presupuesto, parent, false);
        return new PresupuestoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PresupuestoHolder holder, int position) {
        Presupuesto presupuestoActual = listaPresupuestos.get(position);
        holder.tvNombre.setText(presupuestoActual.nombre);
        holder.tvSueldoBase.setText(String.format("Sueldo inicial: $%,.0f", presupuestoActual.sueldoBase));
    }

    @Override
    public int getItemCount() {
        if (listaPresupuestos == null) return 0;
        return listaPresupuestos.size();
    }

    public void setPresupuestos(List<Presupuesto> presupuestos) {
        if (presupuestos != null) {
            this.listaPresupuestos = presupuestos;
        } else {
            this.listaPresupuestos = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    class PresupuestoHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre;
        private TextView tvSueldoBase;

        public PresupuestoHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombrePresupuesto);
            tvSueldoBase = itemView.findViewById(R.id.tvSueldoBasePresupuesto);

            // Clic normal: Entrar a la carpeta
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(listaPresupuestos.get(position));
                }
            });

            // Clic largo: Borrar la carpeta
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                    longClickListener.onItemLongClick(listaPresupuestos.get(position));
                    return true;
                }
                return false;
            });
        }
    }
}