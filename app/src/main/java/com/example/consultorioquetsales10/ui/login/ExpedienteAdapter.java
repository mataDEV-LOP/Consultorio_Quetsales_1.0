package com.example.consultorioquetsales10.ui.login;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consultorioquetsales10.Expediente;
import com.example.consultorioquetsales10.R;
import com.example.consultorioquetsales10.ui.login.DetalleExpedienteActivity;

import java.util.ArrayList;
import java.util.List;

public class ExpedienteAdapter extends RecyclerView.Adapter<ExpedienteAdapter.ExpedienteViewHolder> {
    private List<Expediente> listaExpedientes;
    private Context contexto;

    public ExpedienteAdapter(Context contexto) {
        this.contexto = contexto;
        this.listaExpedientes = new ArrayList<>();
    }

    public void setListaExpedientes(List<Expediente> listaExpedientes) {
        this.listaExpedientes = listaExpedientes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpedienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paciente, parent, false);
        return new ExpedienteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpedienteViewHolder holder, int posicion) {
        Expediente expediente = listaExpedientes.get(posicion);
        holder.tvId.setText(expediente.getId());
        holder.tvNombre.setText(expediente.getNombrePaciente());

        // Configurar el clic en el item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contexto, DetalleExpedienteActivity.class);
                intent.putExtra("expediente", expediente); // Aquí está el problema si Expediente no es Serializable
                contexto.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaExpedientes.size();
    }

    public static class ExpedienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvNombre;

        public ExpedienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNombre = itemView.findViewById(R.id.tvNombre);
        }
    }
}