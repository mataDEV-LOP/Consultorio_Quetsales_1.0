package com.example.consultorioquetsales10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends Fragment {

    // Parámetros (mantenidos por si los necesitas en el futuro)
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Componentes del UI
    private RecyclerView recyclerViewServicios;
    private ServicioAdapter adaptadorServicios;

    public ServiceFragment() {
        // Constructor vacío requerido
    }

    // Factory method (mantenido por si lo necesitas en el futuro)
    public static ServiceFragment newInstance(String param1, String param2) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View vista = inflater.inflate(R.layout.fragment_service, container, false);

        // Inicializar el RecyclerView
        recyclerViewServicios = vista.findViewById(R.id.recyclerViewServicios);
        recyclerViewServicios.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewServicios.setHasFixedSize(true); // Mejorar rendimiento
        recyclerViewServicios.setNestedScrollingEnabled(false); // Desactivar desplazamiento del RecyclerView

        // Crear una lista de servicios de ejemplo
        List<Servicio> listaServicios = new ArrayList<>();
        listaServicios.add(new Servicio(R.drawable.primer_card, "Análisis clínicos de sangre", "Glucosa"));
        listaServicios.add(new Servicio(R.drawable.segunda_card, "Análisis clínicos de sangre", "Biométrica hemática"));
        // Agrega más servicios si es necesario (puedes cargar desde una API o base de datos)
        // Ejemplo: listaServicios.add(new Servicio(R.drawable.trecer_card_cuarta, "Análisis clínicos de sangre", "Otro análisis"));

        // Configurar el adaptador
        adaptadorServicios = new ServicioAdapter(getContext(), listaServicios);
        recyclerViewServicios.setAdapter(adaptadorServicios);

        return vista;
    }
}