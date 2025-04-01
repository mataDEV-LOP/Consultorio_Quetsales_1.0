package com.example.consultorioquetsales10;

import android.content.Intent;
import android.content.SharedPreferences;
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

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

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

        recyclerViewExpedientes = vista.findViewById(R.id.recyclerViewExpedientes);
        btnExpedientes = vista.findViewById(R.id.btnExpedinetes);
        btnCrearReceta = vista.findViewById(R.id.BtnNuevaReceta);

        if (btnExpedientes == null || btnCrearReceta == null) {
            Log.e(TAG, "Uno de los botones es null. Verifica los IDs en fragment_expedientes_y_recetas.xml");
            Toast.makeText(getContext(), "Error: No se encontraron los botones", Toast.LENGTH_LONG).show();
            return vista;
        }

        recyclerViewExpedientes.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptadorExpedientes = new ExpedienteAdapter(getContext());
        recyclerViewExpedientes.setAdapter(adaptadorExpedientes);

        cargarExpedientes();

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
        SharedPreferences prefs = getActivity().getSharedPreferences("DoctorPrefs", getActivity().MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Error: No se encontró el token. Por favor, inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
            redirectToLogin();
            return;
        }

        apiService.obtenerExpedientes("Bearer " + token).enqueue(new Callback<ExpedienteResponse>() {
            @Override
            public void onResponse(Call<ExpedienteResponse> call, Response<ExpedienteResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    adaptadorExpedientes.setListaExpedientes(response.body().getExpedientes());
                } else if (response.code() == 403) {
                    Toast.makeText(getContext(), "Sesión expirada. Por favor, inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
                    redirectToLogin();
                } else {
                    String mensaje = response.body() != null ? response.body().getMessage() : "Error desconocido";
                    Toast.makeText(getContext(), "Error al cargar expedientes: " + mensaje, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ExpedienteResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void redirectToLogin() {
        SharedPreferences prefs = getActivity().getSharedPreferences("DoctorPrefs", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}