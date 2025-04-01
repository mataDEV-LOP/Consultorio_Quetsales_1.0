package com.example.consultorioquetsales10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {

    private Context context;
    private List<Servicio> listaServicios;

    public ServicioAdapter(Context context, List<Servicio> listaServicios) {
        this.context = context;
        this.listaServicios = listaServicios;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = listaServicios.get(position);
        holder.servicioImagen.setImageResource(servicio.getImagenResId());
        holder.servicioTitulo.setText(servicio.getTitulo());
        holder.servicioDescripcion.setText(servicio.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return listaServicios.size();
    }

    public static class ServicioViewHolder extends RecyclerView.ViewHolder {
        ImageView servicioImagen;
        TextView servicioTitulo;
        TextView servicioDescripcion;
        Button servicioPrecio;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            servicioImagen = itemView.findViewById(R.id.servicioImagen);
            servicioTitulo = itemView.findViewById(R.id.servicioTitulo);
            servicioDescripcion = itemView.findViewById(R.id.servicioDescripcion);
            servicioPrecio = itemView.findViewById(R.id.servicioPrecio);
        }
    }
}