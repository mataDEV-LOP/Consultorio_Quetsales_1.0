package com.example.consultorioquetsales10;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consultorioquetsales10.ui.login.ExpedienteAdapter;

public class ExpedientesYRecetasFragment extends Fragment {

    private static final String TAG = "ExpedientesYRecetasFragment";
    private RecyclerView recyclerViewExpedientes;
    private ExpedienteAdapter adaptadorExpedientes;
    private Button btnExpedientes, btnCrearReceta;
    private ActivityResultLauncher<Intent> lanzadorNuevoExpediente;
    private ActivityResultLauncher<Intent> lanzadorNuevaReceta;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lanzador para NuevoExpedienteActivity
        lanzadorNuevoExpediente = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                resultado -> {
                    if (resultado.getResultCode() == getActivity().RESULT_OK) {
                        Log.d(TAG, "Resultado OK de NuevoExpedienteActivity, actualizando lista...");
                        adaptadorExpedientes.setListaExpedientes(NuevoExpedienteActivity.getListaExpedientes());
                    } else {
                        Log.d(TAG, "Resultado no OK de NuevoExpedienteActivity: " + resultado.getResultCode());
                    }
                }
        );

        // Lanzador para FormRecetaActivity
        lanzadorNuevaReceta = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                resultado -> {
                    if (resultado.getResultCode() == getActivity().RESULT_OK) {
                        Log.d(TAG, "Resultado OK de FormRecetaActivity");
                        // Aquí puedes manejar el resultado de FormRecetaActivity si es necesario
                    } else {
                        Log.d(TAG, "Resultado no OK de FormRecetaActivity: " + resultado.getResultCode());
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_expedientes_y_recetas, container, false);

        recyclerViewExpedientes = vista.findViewById(R.id.recyclerViewExpedientes);
        btnExpedientes = vista.findViewById(R.id.btnExpedinetes); // Corregimos el ID
        btnCrearReceta = vista.findViewById(R.id.BtnNuevaReceta); // Usamos el ID correcto del XML

        // Verificar si los botones se inicializaron correctamente
        if (btnExpedientes == null) {
            Log.e(TAG, "btnExpedientes es null. Verifica el ID en fragment_expedientes_y_recetas.xml");
            Toast.makeText(getContext(), "Error: No se encontró el botón de Expedientes", Toast.LENGTH_LONG).show();
            return vista;
        }
        if (btnCrearReceta == null) {
            Log.e(TAG, "btnCrearReceta es null. Verifica el ID en fragment_expedientes_y_recetas.xml");
            Toast.makeText(getContext(), "Error: No se encontró el botón de Crear Receta", Toast.LENGTH_LONG).show();
            return vista;
        }

        recyclerViewExpedientes.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptadorExpedientes = new ExpedienteAdapter(getContext()); // Pasar el contexto al adaptador
        recyclerViewExpedientes.setAdapter(adaptadorExpedientes);

        adaptadorExpedientes.setListaExpedientes(NuevoExpedienteActivity.getListaExpedientes());

        // Configurar el botón de Expedientes
        btnExpedientes.setOnClickListener(v -> {
            Log.d(TAG, "Botón Expedientes presionado");
            Intent intent = new Intent(getActivity(), NuevoExpedienteActivity.class);
            try {
                lanzadorNuevoExpediente.launch(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error al abrir NuevoExpedienteActivity: " + e.getMessage(), e);
                Toast.makeText(getContext(), "Error al abrir la actividad de Expedientes", Toast.LENGTH_LONG).show();
            }
        });

        // Configurar el botón de Crear Receta
        btnCrearReceta.setOnClickListener(v -> {
            Log.d(TAG, "Botón Crear Receta presionado");
            Intent intent = new Intent(getActivity(), FormRecetaActivity.class);
            try {
                lanzadorNuevaReceta.launch(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error al abrir FormRecetaActivity: " + e.getMessage(), e);
                Toast.makeText(getContext(), "Error al abrir la actividad de Receta", Toast.LENGTH_LONG).show();
            }
        });

        return vista;
    }
}