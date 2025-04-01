package com.example.consultorioquetsales10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.consultorioquetsales10.api.ApiService;
import com.example.consultorioquetsales10.api.LoginRequest;
import com.example.consultorioquetsales10.model.LoginResponse;
import com.example.consultorioquetsales10.network.RetrofitClient;
import com.example.consultorioquetsales10.utils.LocaleHelper;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText login_doctor_id, login_password;
    Button login_button;
    TextView Bienvenido, dialog_language, Huella;
    ImageButton btnIdioma;
    int lang_selected;
    RelativeLayout show_lan_dialog;
    Context context;
    Resources resources;
    ApiService apiService;

    private static final String PREFS_NAME = "DoctorPrefs";
    private static final String KEY_DOCTOR_ID = "doctorId";
    private static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializar el idioma usando LocaleHelper
        context = LocaleHelper.onAttach(this);
        resources = context.getResources();
        setContentView(R.layout.activity_login);

        login_doctor_id = findViewById(R.id.login_doctor_id);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
        Bienvenido = findViewById(R.id.Bienvenido);
        dialog_language = findViewById(R.id.dialog_language);
        show_lan_dialog = findViewById(R.id.showlangdialog);
        Huella = findViewById(R.id.huella_button);

        // Configurar Retrofit
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Configuración inicial del idioma
        if (LocaleHelper.getLanguage(this).equalsIgnoreCase("es")) {
            lang_selected = 0;
            dialog_language.setText("Español");
            Bienvenido.setText(resources.getString(R.string.title_welcome));
        } else if (LocaleHelper.getLanguage(this).equalsIgnoreCase("en")) {
            lang_selected = 1;
            dialog_language.setText("English");
            Bienvenido.setText(resources.getString(R.string.welcome));
        }

        show_lan_dialog.setOnClickListener(view -> {
            final String[] Language = {"Español", "English"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Selecciona un Idioma")
                    .setSingleChoiceItems(Language, lang_selected, (dialogInterface, i) -> {
                        dialog_language.setText(Language[i]);
                        if (Language[i].equals("Español")) {
                            context = LocaleHelper.setLocale(this, "es");
                            resources = context.getResources();
                            lang_selected = 0;
                            Bienvenido.setText(resources.getString(R.string.title_welcome));
                            login_button.setText(resources.getString(R.string.login_button));
                            Huella.setText(resources.getString(R.string.huella_button));
                        }
                        if (Language[i].equals("English")) {
                            context = LocaleHelper.setLocale(this, "en");
                            resources = context.getResources();
                            lang_selected = 1;
                            Bienvenido.setText(resources.getString(R.string.welcome));
                            login_button.setText(resources.getString(R.string.login_button));
                            Huella.setText(resources.getString(R.string.huella_button));
                        }
                    })
                    .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
        });

        // Login con ID y contraseña
        login_button.setOnClickListener(view -> {
            if (!validateDoctorId() || !validatePassword()) {
                return;
            }

            String doctorId = login_doctor_id.getText().toString().trim();
            String password = login_password.getText().toString().trim();
            LoginRequest loginRequest = new LoginRequest(doctorId, password);

            apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse.isSuccess()) {
                            saveDoctorCredentials(doctorId, loginResponse.getToken());
                            showSuccessDialog();
                        } else {
                            showDeniedDialog();
                        }
                    } else {
                        showToast("Error en la respuesta del servidor");
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    showToast("Error de conexión: " + t.getMessage());
                }
            });
        });

        // Login con huella digital
        Button btnHuella = findViewById(R.id.huella_button);
        btnHuella.setOnClickListener(view -> {
            String savedDoctorId = getDoctorId();
            String savedToken = getToken();
            if (savedDoctorId == null || savedToken == null) {
                showToast("Por favor, inicia sesión con tu ID y contraseña primero");
                return;
            }
            showBiometricPrompt(savedDoctorId, savedToken);
        });
    }

    public Boolean validateDoctorId() {
        String val = login_doctor_id.getText().toString().trim();
        if (val.isEmpty()) {
            login_doctor_id.setError("El ID del doctor no debe estar vacío");
            return false;
        } else {
            login_doctor_id.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = login_password.getText().toString().trim();
        if (val.isEmpty()) {
            login_password.setError("La contraseña no debe estar vacía");
            return false;
        } else {
            login_password.setError(null);
            return true;
        }
    }

    private void saveDoctorCredentials(String doctorId, String token) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_DOCTOR_ID, doctorId);
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    private String getDoctorId() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(KEY_DOCTOR_ID, null);
    }

    private String getToken() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, null);
    }

    private void clearDoctorCredentials() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_DOCTOR_ID);
        editor.remove(KEY_TOKEN);
        editor.apply();
    }

    private void showSuccessDialog() {
        ConstraintLayout successConstraintLayout = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(this).inflate(R.layout.success_dialog, successConstraintLayout);
        Button successDone = view.findViewById(R.id.successDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        successDone.setOnClickListener(v -> {
            alertDialog.dismiss();
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showDeniedDialog() {
        ConstraintLayout deniedConstraintLayout = findViewById(R.id.deniedConstraintLayout);
        View view = LayoutInflater.from(this).inflate(R.layout.login_denied_dialog, deniedConstraintLayout);
        Button deniedDone = view.findViewById(R.id.deniedDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        deniedDone.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showBiometricPrompt(String doctorId, String token) {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                showToast("Autenticación error: " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Aquí podrías validar el token con el backend si fuera necesario
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                showToast("Autenticación fallida");
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación biométrica")
                .setSubtitle("Usa tu huella digital para continuar")
                .setNegativeButtonText("Cancelar")
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}