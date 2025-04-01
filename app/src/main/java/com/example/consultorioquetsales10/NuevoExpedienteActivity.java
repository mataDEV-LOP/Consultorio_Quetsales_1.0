package com.example.consultorioquetsales10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.consultorioquetsales10.api.ApiService;
import com.example.consultorioquetsales10.model.ExpedienteResponse;
import com.example.consultorioquetsales10.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoExpedienteActivity extends AppCompatActivity {

    private EditText etNombre, etFechaNacimiento, etEdad, etTelefono, etEmail, etAlergias, etTemperatura, etPresionArterial;
    private Button btnGuardar, btnCancelar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_expedientes);

        etNombre = findViewById(R.id.etNombre);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        etEdad = findViewById(R.id.etEdad);
        etTelefono = findViewById(R.id.etTelefono);
        etEmail = findViewById(R.id.etEmail);
        etAlergias = findViewById(R.id.etAlergias);
        etTemperatura = findViewById(R.id.etTemperatura);
        etPresionArterial = findViewById(R.id.etPresionArterial);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        btnGuardar.setOnClickListener(v -> guardarExpediente());

        btnCancelar.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void guardarExpediente() {
        String nombre = etNombre.getText().toString().trim();
        String fechaNacimiento = etFechaNacimiento.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String alergias = etAlergias.getText().toString().trim();
        String temperaturaStr = etTemperatura.getText().toString().trim();
        String presionArterial = etPresionArterial.getText().toString().trim();

        if (nombre.isEmpty() || fechaNacimiento.isEmpty() || edadStr.isEmpty() || telefono.isEmpty() ||
                email.isEmpty() || alergias.isEmpty() || temperaturaStr.isEmpty() || presionArterial.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fechaNacimiento.matches("\\d{2}/\\d{2}/\\d{4}")) {
            Toast.makeText(this, "La fecha debe estar en formato DD/MM/AAAA", Toast.LENGTH_SHORT).show();
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
            if (edad < 0 || edad > 150) {
                Toast.makeText(this, "La edad debe estar entre 0 y 150", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La edad debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!telefono.matches("\\d{10}")) {
            Toast.makeText(this, "El teléfono debe tener 10 dígitos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor, ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;
        }

        double temperatura;
        try {
            temperatura = Double.parseDouble(temperaturaStr);
            if (temperatura < 30 || temperatura > 45) {
                Toast.makeText(this, "La temperatura debe estar entre 30 y 45 °C", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La temperatura debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!presionArterial.matches("\\d{2,3}/\\d{2,3}")) {
            Toast.makeText(this, "La presión arterial debe estar en formato 120/80", Toast.LENGTH_SHORT).show();
            return;
        }

        Expediente expediente = new Expediente(
                null,
                nombre,
                fechaNacimiento,
                edad,
                telefono,
                email,
                alergias,
                temperatura,
                presionArterial
        );

        SharedPreferences prefs = getSharedPreferences("DoctorPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Error: No se encontró el token. Por favor, inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
            redirectToLogin();
            return;
        }

        apiService.guardarExpediente("Bearer " + token, expediente).enqueue(new Callback<ExpedienteResponse>() {
            @Override
            public void onResponse(Call<ExpedienteResponse> call, Response<ExpedienteResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(NuevoExpedienteActivity.this, "Expediente guardado con éxito", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else if (response.code() == 403) {
                    Toast.makeText(NuevoExpedienteActivity.this, "Sesión expirada. Por favor, inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
                    redirectToLogin();
                } else {
                    String mensaje = response.body() != null ? response.body().getMessage() : "Error desconocido";
                    Toast.makeText(NuevoExpedienteActivity.this, "Error al guardar: " + mensaje, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ExpedienteResponse> call, Throwable t) {
                Toast.makeText(NuevoExpedienteActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void redirectToLogin() {
        SharedPreferences prefs = getSharedPreferences("DoctorPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}