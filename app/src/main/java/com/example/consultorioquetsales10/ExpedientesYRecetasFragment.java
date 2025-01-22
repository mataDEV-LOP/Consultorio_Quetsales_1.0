package com.example.consultorioquetsales10;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;

public class ExpedientesYRecetasFragment extends Fragment {
    private Button btnExpedientes;
    private Button BtnNuevaReceta;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expedientes_y_recetas, container, false);

        btnExpedientes = view.findViewById(R.id.btnExpedinetes);
        BtnNuevaReceta = view.findViewById(R.id.BtnNuevaReceta); // Corrected ID

        btnExpedientes.setOnClickListener(v -> {
            if (getActivity() instanceof MenuActivity) {
                ((MenuActivity) getActivity()).openExpedientesFragment();
            }
        });

        BtnNuevaReceta.setOnClickListener(v -> {
            Log.d("RecetasFragment", "Botón Nuevo Expediente pulsado");
            if (getActivity() instanceof MenuActivity) {
                ((MenuActivity) getActivity()).openRecetasFragment(); // Call a different method
            }
        });

        return view;
    }
}
