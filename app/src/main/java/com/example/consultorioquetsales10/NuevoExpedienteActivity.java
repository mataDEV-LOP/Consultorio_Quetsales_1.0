package com.example.consultorioquetsales10;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NuevoExpedienteActivity extends AppCompatActivity {

    private EditText etNombre, etFechaNacimiento, etEdad, etTelefono, etEmail, etAlergias, etTemperatura, etPresionArterial;
    private Button btnGuardar, btnCancelar;
    private Calendar fechaSeleccionada = Calendar.getInstance();
    private static List<Expediente> listaExpedientes = new ArrayList<>();
    private static int contadorId = 1;

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

        etFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorFecha();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarExpediente();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mostrarSelectorFecha() {
        Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog selectorFecha = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int anio, int mes, int diaDelMes) {
                        fechaSeleccionada.set(anio, mes, diaDelMes);

                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        etFechaNacimiento.setText(formatoFecha.format(fechaSeleccionada.getTime()));

                        Calendar hoy = Calendar.getInstance();
                        int edad = hoy.get(Calendar.YEAR) - anio;

                        if (hoy.get(Calendar.DAY_OF_YEAR) < fechaSeleccionada.get(Calendar.DAY_OF_YEAR)) {
                            edad--;
                        }

                        etEdad.setText(String.valueOf(edad));
                    }
                },
                anio,
                mes,
                dia
        );

        selectorFecha.show();
    }

    private void guardarExpediente() {
        if (etNombre.getText().toString().trim().isEmpty()) {
            etNombre.setError("Este campo es obligatorio");
            return;
        }

        String id = String.format("%03d", contadorId++);
        Expediente expediente = new Expediente(
                id,
                etNombre.getText().toString(),
                etFechaNacimiento.getText().toString(),
                etEdad.getText().toString().isEmpty() ? 0 : Integer.parseInt(etEdad.getText().toString()),
                etTelefono.getText().toString(),
                etEmail.getText().toString(),
                etAlergias.getText().toString(),
                etTemperatura.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(etTemperatura.getText().toString()),
                etPresionArterial.getText().toString()
        );

        listaExpedientes.add(expediente);

        Toast.makeText(this, "Expediente guardado correctamente", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("nuevoExpediente", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static List<Expediente> getListaExpedientes() {
        return listaExpedientes;
    }
}