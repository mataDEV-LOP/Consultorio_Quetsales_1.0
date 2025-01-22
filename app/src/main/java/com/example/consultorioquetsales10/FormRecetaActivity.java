package com.example.consultorioquetsales10;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FormRecetaActivity extends AppCompatActivity {

    private EditText editTextNombrePaciente, editTextFechaCreacion, editTextFechaCaducidad, editTextDiagnostico, editTextTratamiento, editTextComentarios;
    private Button buttonDescargarPdf;
    private Button buttonCancelarPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_receta);

        // Referencias de los campos de texto
        editTextNombrePaciente = findViewById(R.id.editTextNombrePaciente);
        editTextFechaCreacion = findViewById(R.id.editTextFechaCreacion);
        editTextFechaCaducidad = findViewById(R.id.editTextFechaCaducidad);
        editTextDiagnostico = findViewById(R.id.editTextDiagnostico);
        editTextTratamiento = findViewById(R.id.editTextTratamiento);
        editTextComentarios = findViewById(R.id.editTextComentarios);
        buttonDescargarPdf = findViewById(R.id.buttonDescargarPdf);

        // Solicitar permisos de almacenamiento en caso de ser necesario
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        // Configurar el botón para generar PDF
        buttonDescargarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPDF();
            }
        });

        //Botón de cancelar
        buttonCancelarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(FormRecetaActivity.this, ExpedientesYRecetasFragment.class);
                startActivity(intent);
            }
        });
    }

    private void generarPDF() {
        // Crear un nuevo documento PDF
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        // Crear una página en el documento
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // Tamaño A4 en puntos (595 x 842)
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Obtener los datos de los campos de texto
        String nombrePaciente = "Nombre del Paciente: " + editTextNombrePaciente.getText().toString();
        String fechaCreacion = "Fecha de Creación: " + editTextFechaCreacion.getText().toString();
        String fechaCaducidad = "Fecha de Caducidad: " + editTextFechaCaducidad.getText().toString();
        String diagnostico = "Diagnóstico: " + editTextDiagnostico.getText().toString();
        String tratamiento = "Tratamiento: " + editTextTratamiento.getText().toString();
        String comentarios = "Comentarios: " + editTextComentarios.getText().toString();

        // Escribir texto en el PDF
        int yPos = 40;
        paint.setTextSize(16);
        canvas.drawText("Formulario de Paciente", 200, yPos, paint);

        yPos += 40;
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

        // Guardar el archivo PDF
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FormularioPaciente.pdf";
        File file = new File(filePath);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF guardado en: " + filePath, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar el PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Cerrar el documento PDF
        pdfDocument.close();
    }

    // Manejo del resultado del permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de almacenamiento concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}