package com.example.consultorioquetsales10;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FormRecetaActivity extends AppCompatActivity {

    private static final String TAG = "FormRecetaActivity";
    private EditText editTextFechaCreacion, editTextFechaCaducidad, editCedula, editTextNombreDoctor,
            editTextNombrePaciente, editTextDiagnostico, editTextTratamiento, editTextComentarios;
    private Button buttonDescargarPdf, buttonCancelarPdf;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_receta);

        // Inicializar vistas
        editTextFechaCreacion = findViewById(R.id.editTextFechaCreacion);
        editTextFechaCaducidad = findViewById(R.id.editTextFechaCaducidad);
        editCedula = findViewById(R.id.editcedula);
        editTextNombreDoctor = findViewById(R.id.editTextNombreDoctor);
        editTextNombrePaciente = findViewById(R.id.editTextNombrePaciente);
        editTextDiagnostico = findViewById(R.id.editTextDiagnostico);
        editTextTratamiento = findViewById(R.id.editTextTratamiento);
        editTextComentarios = findViewById(R.id.editTextComentarios);
        buttonDescargarPdf = findViewById(R.id.buttonDescargarPdf);
        buttonCancelarPdf = findViewById(R.id.buttonCancelarPdf);

        // Verificar si los botones se inicializaron correctamente
        if (buttonDescargarPdf == null) {
            Log.e(TAG, "buttonDescargarPdf es null. Verifica el ID en activity_form_receta.xml");
            Toast.makeText(this, "Error: No se encontró el botón de Descargar PDF", Toast.LENGTH_LONG).show();
            return;
        }
        if (buttonCancelarPdf == null) {
            Log.e(TAG, "buttonCancelarPdf es null. Verifica el ID en activity_form_receta.xml");
            Toast.makeText(this, "Error: No se encontró el botón de Cancelar", Toast.LENGTH_LONG).show();
            return;
        }

        calendar = Calendar.getInstance();

        // Configurar selector de fechas para Fecha de Creación
        editTextFechaCreacion.setOnClickListener(v -> mostrarSelectorFecha(editTextFechaCreacion));

        // Configurar selector de fechas para Fecha de Caducidad
        editTextFechaCaducidad.setOnClickListener(v -> mostrarSelectorFecha(editTextFechaCaducidad));

        // Configurar botón de Descargar PDF
        buttonDescargarPdf.setOnClickListener(v -> {
            Log.d(TAG, "Botón Descargar PDF presionado");
            if (validarCampos()) {
                Log.d(TAG, "Campos validados correctamente, generando PDF...");
                generarPDF();
            } else {
                Log.d(TAG, "Validación de campos fallida");
                Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar botón de Cancelar
        buttonCancelarPdf.setOnClickListener(v -> {
            Log.d(TAG, "Botón Cancelar presionado");
            finish();
        });
    }

    private void mostrarSelectorFecha(EditText editText) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, yearSelected, monthSelected, dayOfMonth) -> {
                    calendar.set(yearSelected, monthSelected, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    editText.setText(sdf.format(calendar.getTime()));
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private boolean validarCampos() {
        Log.d(TAG, "Validando campos...");
        if (editTextFechaCreacion.getText().toString().trim().isEmpty()) {
            editTextFechaCreacion.setError("Este campo es obligatorio");
            Log.d(TAG, "Fecha de Creación vacía");
            return false;
        }
        if (editTextFechaCaducidad.getText().toString().trim().isEmpty()) {
            editTextFechaCaducidad.setError("Este campo es obligatorio");
            Log.d(TAG, "Fecha de Caducidad vacía");
            return false;
        }
        if (editCedula.getText().toString().trim().isEmpty()) {
            editCedula.setError("Este campo es obligatorio");
            Log.d(TAG, "Cédula vacía");
            return false;
        }
        if (editTextNombreDoctor.getText().toString().trim().isEmpty()) {
            editTextNombreDoctor.setError("Este campo es obligatorio");
            Log.d(TAG, "Nombre del Doctor vacío");
            return false;
        }
        if (editTextNombrePaciente.getText().toString().trim().isEmpty()) {
            editTextNombrePaciente.setError("Este campo es obligatorio");
            Log.d(TAG, "Nombre del Paciente vacío");
            return false;
        }
        if (editTextDiagnostico.getText().toString().trim().isEmpty()) {
            editTextDiagnostico.setError("Este campo es obligatorio");
            Log.d(TAG, "Diagnóstico vacío");
            return false;
        }
        if (editTextTratamiento.getText().toString().trim().isEmpty()) {
            editTextTratamiento.setError("Este campo es obligatorio");
            Log.d(TAG, "Tratamiento vacío");
            return false;
        }
        Log.d(TAG, "Todos los campos validados correctamente");
        return true;
    }

    private void generarPDF() {
        Log.d(TAG, "Iniciando generación del PDF...");
        // Crear un nuevo documento PDF
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        // Crear una página en el documento
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // Tamaño A4 en puntos (595 x 842)
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Obtener los datos de los campos de texto
        String cedula = "Cédula del Doctor: " + editCedula.getText().toString();
        String nombreDoctor = "Nombre del Doctor: " + editTextNombreDoctor.getText().toString();
        String nombrePaciente = "Nombre del Paciente: " + editTextNombrePaciente.getText().toString();
        String fechaCreacion = "Fecha de Creación: " + editTextFechaCreacion.getText().toString();
        String fechaCaducidad = "Fecha de Caducidad: " + editTextFechaCaducidad.getText().toString();
        String diagnostico = "Diagnóstico: " + editTextDiagnostico.getText().toString();
        String tratamiento = "Tratamiento: " + editTextTratamiento.getText().toString();
        String comentarios = "Comentarios: " + (editTextComentarios.getText().toString().isEmpty() ? "Ninguno" : editTextComentarios.getText().toString());

        // Escribir texto en el PDF
        int yPos = 40;
        paint.setTextSize(16);
        canvas.drawText("Receta Médica", 220, yPos, paint);

        yPos += 40;
        canvas.drawText(cedula, 20, yPos, paint);

        yPos += 30;
        canvas.drawText(nombreDoctor, 20, yPos, paint);

        yPos += 30;
        canvas.drawText(nombrePaciente, 20, yPos, paint);

        yPos += 30;
        canvas.drawText(fechaCreacion, 20, yPos, paint);

        yPos += 30;
        canvas.drawText(fechaCaducidad, 20, yPos, paint);

        yPos += 30;
        canvas.drawText(diagnostico, 20, yPos, paint);

        yPos += 30;
        canvas.drawText(tratamiento, 20, yPos, paint);

        yPos += 30;
        canvas.drawText(comentarios, 20, yPos, paint);

        // Terminar la página
        pdfDocument.finishPage(page);

        // Guardar el archivo PDF en el directorio interno de la app (no requiere permisos)
        File dir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Recetas");
        if (!dir.exists()) {
            Log.d(TAG, "Creando directorio: " + dir.getAbsolutePath());
            boolean created = dir.mkdirs(); // Crear el directorio si no existe
            if (!created) {
                Log.e(TAG, "No se pudo crear el directorio: " + dir.getAbsolutePath());
                Toast.makeText(this, "Error: No se pudo crear el directorio para guardar el PDF", Toast.LENGTH_LONG).show();
                pdfDocument.close();
                return;
            }
        }

        String fileName = "RecetaMedica_" + System.currentTimeMillis() + ".pdf";
        File file = new File(dir, fileName);
        Log.d(TAG, "Guardando PDF en: " + file.getAbsolutePath());

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Log.d(TAG, "PDF guardado exitosamente");
            Toast.makeText(this, "PDF guardado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "Error al guardar el PDF: " + e.getMessage(), e);
            Toast.makeText(this, "Error al guardar el PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            // Cerrar el documento PDF
            pdfDocument.close();
            Log.d(TAG, "Documento PDF cerrado");
        }
    }
}