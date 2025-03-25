package com.example.consultorioquetsales10.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consultorioquetsales10.Expediente;
import com.example.consultorioquetsales10.R;

public class DetalleExpedienteActivity extends AppCompatActivity {

    private TextView tvNombrePaciente, tvFechaNacimiento, tvEdad, tvTelefono, tvCorreo, tvAlergias, tvTemperatura, tvPresionArterial;
    private ImageView btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_expediente);

        // Inicializar vistas
        tvNombrePaciente = findViewById(R.id.patientName);
        tvFechaNacimiento = findViewById(R.id.birthDate);
        tvEdad = findViewById(R.id.age);
        tvTelefono = findViewById(R.id.phone);
        tvCorreo = findViewById(R.id.email);
        tvAlergias = findViewById(R.id.allergies);
        tvTemperatura = findViewById(R.id.temperature);
        tvPresionArterial = findViewById(R.id.bloodPressure);
        btnRegresar = findViewById(R.id.backBtn);

        // Obtener datos del Intent
        Expediente expediente = (Expediente) getIntent().getSerializableExtra("expediente");

        if (expediente != null) {
            // Mostrar los datos en las vistas
            tvNombrePaciente.setText(expediente.getNombrePaciente());
            tvFechaNacimiento.setText(expediente.getFechaNacimiento());
            tvEdad.setText(expediente.getEdad() + " años");
            tvTelefono.setText(expediente.getTelefono());
            tvCorreo.setText(expediente.getCorreoElectronico());
            tvAlergias.setText(expediente.getAlergias());
            tvTemperatura.setText(expediente.getTemperatura() + " °C");
            tvPresionArterial.setText(expediente.getPresionArterial() + " mmHg");
        }

        // Configurar el botón de regresar
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}