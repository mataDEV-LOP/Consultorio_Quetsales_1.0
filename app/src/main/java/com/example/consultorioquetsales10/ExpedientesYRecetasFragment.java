package com.example.consultorioquetsales10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
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

import com.example.consultorioquetsales10.api.ApiService;
import com.example.consultorioquetsales10.model.ExpedienteResponse;
import com.example.consultorioquetsales10.network.RetrofitClient;
import com.example.consultorioquetsales10.ui.login.ExpedienteAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpedientesYRecetasFragment extends Fragment {

    private static final String TAG = "ExpedientesYRecetasFragment";
    private RecyclerView recyclerViewExpedientes;
    private ExpedienteAdapter adaptadorExpedientes;
    private Button btnExpedientes, btnCrearReceta;
    private ActivityResultLauncher<Intent> lanzadorNuevoExpediente;
    private ActivityResultLauncher<Intent> lanzadorNuevaReceta;
    private ApiService apiService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar el servicio de API
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Configurar el lanzador para NuevoExpedienteActivity
        lanzadorNuevoExpediente = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                resultado -> {
                    if (resultado.getResultCode() == getActivity().RESULT_OK) {
                        Log.d(TAG, "Resultado OK de NuevoExpedienteActivity, actualizando lista...");
                        cargarExpedientes();
                    } else {
                        Log.d(TAG, "Resultado no OK de NuevoExpedienteActivity: " + resultado.getResultCode());
                    }
                }
        );

        // Configurar el lanzador para FormRecetaActivity
        lanzadorNuevaReceta = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                resultado -> {
                    if (resultado.getResultCode() == getActivity().RESULT_OK) {
                        Log.d(TAG, "Resultado OK de FormRecetaActivity");
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

        // Inicializar vistas
        recyclerViewExpedientes = vista.findViewById(R.id.recyclerViewExpedientes);
        btnExpedientes = vista.findViewById(R.id.btnExpedinetes);
        btnCrearReceta = vista.findViewById(R.id.BtnNuevaReceta);

        // Verificar que los botones no sean null
        if (btnExpedientes == null || btnCrearReceta == null) {
            Log.e(TAG, "Uno de los botones es null. Verifica los IDs en fragment_expedientes_y_recetas.xml");
            Toast.makeText(getContext(), "Error: No se encontraron los botones", Toast.LENGTH_LONG).show();
            return vista;
        }

        // Configurar el RecyclerView
        recyclerViewExpedientes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewExpedientes.setHasFixedSize(true); // Mejorar rendimiento
        recyclerViewExpedientes.setNestedScrollingEnabled(false); // Desactivar desplazamiento del RecyclerView

        // Añadir espaciado entre los elementos del RecyclerView (16dp)
        recyclerViewExpedientes.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 16; // Espaciado de 16dp entre los elementos
            }
        });

        // Inicializar y configurar el adaptador
        adaptadorExpedientes = new ExpedienteAdapter(getContext());
        recyclerViewExpedientes.setAdapter(adaptadorExpedientes);

        // Cargar los expedientes desde la API
        cargarExpedientes();

        // Configurar el listener para el botón "Nuevo Expediente"
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

        // Configurar el listener para el botón "Crear Receta"
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

    private void cargarExpedientes() {
        // Obtener el token desde SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("DoctorPrefs", getActivity().MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Error: No se encontró el token. Por favor, inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
            redirectToLogin();
            return;
        }

        // Hacer la llamada a la API para obtener los expedientes
        apiService.obtenerExpedientes("Bearer " + token).enqueue(new Callback<ExpedienteResponse>() {
            @Override
            public void onResponse(Call<ExpedienteResponse> call, Response<ExpedienteResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // Actualizar el adaptador con la lista de expedientes
                    adaptadorExpedientes.setListaExpedientes(response.body().getExpedientes());
                    Log.d(TAG, "Expedientes cargados exitosamente: " + response.body().getExpedientes().size());
                } else if (response.code() == 403) {
                    Toast.makeText(getContext(), "Sesión expirada. Por favor, inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
                    redirectToLogin();
                } else {
                    String mensaje = response.body() != null ? response.body().getMessage() : "Error desconocido";
                    Toast.makeText(getContext(), "Error al cargar expedientes: " + mensaje, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error al cargar expedientes: " + mensaje);
                }
            }

            @Override
            public void onFailure(Call<ExpedienteResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error de conexión al cargar expedientes: " + t.getMessage(), t);
            }
        });
    }

    private void redirectToLogin() {
        // Limpiar SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("DoctorPrefs", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // Redirigir a la actividad de login
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}