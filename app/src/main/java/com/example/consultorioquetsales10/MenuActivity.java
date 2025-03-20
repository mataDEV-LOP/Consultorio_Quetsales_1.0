package com.example.consultorioquetsales10;

import static com.example.consultorioquetsales10.R.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import android.Manifest;
import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class MenuActivity extends AppCompatActivity {
    private int selectedTab = 1;
    private ExpedientesYRecetasFragment expedientesYRecetasFragment;
    private AgendaFragment agendaFragment;
    private EditText editTextNombrePaciente, editTextFechaCreacion, editTextFechaCaducidad, editTextDiagnostico, editTextTratamiento, editTextComentarios;
    private Button buttonDescargarPdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        final LinearLayout ExpedientesYRecetasLayout = findViewById(R.id.ExpedientesYRecetasLayout);
        final LinearLayout ServicesLayout = findViewById(R.id.ServicesLayout);
        final LinearLayout AgendaLayout = findViewById(R.id.AgendaLayout);
        final LinearLayout top_layout = findViewById(R.id.top_layout);

        final ImageView ExpedientesYRecetasImage = findViewById(R.id.ExpedientesYRecetasImage);
        final ImageView ServicesImage = findViewById(R.id.ServicesImage);
        final ImageView AgendaImage = findViewById(R.id.AgendaImage);
        final ImageView top_layout_image = findViewById(R.id.top_layout_image);

        final TextView ExpedientesYRecetasText = findViewById(R.id.ExpedientesYRecetasText);
        final TextView ServicesText = findViewById(R.id.ServicesText);
        final TextView AgendaText = findViewById(R.id.AgendaText);
        final TextView top_layout_txt = findViewById(R.id.top_layout_txt);

        //Establecer el fragment de Servicios por defecto
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, ExpedientesYRecetasFragment.class, null)
                .commit();

        ExpedientesYRecetasLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validar si expedientes ya esta seleccionada
                if (selectedTab != 1) {

                    //Establecer fragment de ExpedientesYRecetas
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ExpedientesYRecetasFragment.class, null)
                            .commit();
                    //change top
                    top_layout_txt.setText("Expedientes y Recetas");
                    top_layout_image.setImageResource(R.drawable.baseline_assignment_24_white);

                    //unselect other tabs expect expedientes tab
                    ServicesText.setVisibility(View.GONE);
                    AgendaText.setVisibility(View.GONE);

                    ServicesImage.setImageResource(R.drawable.baseline_assignment_ind_24);
                    AgendaImage.setImageResource(R.drawable.baseline_calendar_month_24);

                    ServicesLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    AgendaLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //seleccionar la pestaña expedientes
                    ExpedientesYRecetasText.setVisibility(View.VISIBLE);
                    ExpedientesYRecetasImage.setImageResource(R.drawable.baseline_assignment_24);
                    ExpedientesYRecetasLayout.setBackgroundResource(R.drawable.round_back_expedientes_100);

                    //crear animación
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    ExpedientesYRecetasLayout.startAnimation(scaleAnimation);

                    //establecer la primera pestaña como la seleccion actual
                    selectedTab = 1;

                }

            }
        });


        ServicesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validar si Servicios ya esta seleccionada
                if (selectedTab != 2) {
                    //Establecer fragment de Servicios
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ServiceFragment.class, null)
                            .commit();
                    //change top
                    top_layout_txt.setText("Servicios");
                    top_layout_image.setImageResource(R.drawable.baseline_assignment_ind_24_white);

                    //unselect other tabs expect servicios tab
                    ExpedientesYRecetasText.setVisibility(View.GONE);
                    AgendaText.setVisibility(View.GONE);

                    ExpedientesYRecetasImage.setImageResource(R.drawable.baseline_assignment_24);
                    AgendaImage.setImageResource(R.drawable.baseline_calendar_month_24);

                    ExpedientesYRecetasLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    AgendaLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //seleccionar la pestaña servicios
                    ServicesText.setVisibility(View.VISIBLE);
                    ServicesImage.setImageResource(R.drawable.baseline_assignment_ind_24);
                    ServicesLayout.setBackgroundResource(R.drawable.round_back_services_100);

                    //crear animación
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    ServicesLayout.startAnimation(scaleAnimation);

                    //establecer la segunda pestaña como la seleccion actual
                    selectedTab = 2;

                }


            }
        });

        AgendaLayout.setOnClickListener(view -> {
            //Validar si Agenda ya esta seleccionada
            if (selectedTab != 3) {
                //Establecer fragment de Agenda
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new AgendaFragment())
                        .commit();

                //change top
                top_layout_txt.setText("Agenda");
                top_layout_image.setImageResource(R.drawable.baseline_calendar_month_24_white);

                //unselect other tabs expect agenda tab
                ExpedientesYRecetasText.setVisibility(View.GONE);
                ServicesText.setVisibility(View.GONE);

                ExpedientesYRecetasImage.setImageResource(R.drawable.baseline_assignment_24);
                ServicesImage.setImageResource(R.drawable.baseline_assignment_ind_24);

                ExpedientesYRecetasLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                ServicesLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                //seleccionar la pestaña agenda
                AgendaText.setVisibility(View.VISIBLE);
                AgendaImage.setImageResource(R.drawable.baseline_calendar_month_24);
                AgendaLayout.setBackgroundResource(R.drawable.round_back_agenda_100);

                //crear animación
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                AgendaLayout.startAnimation(scaleAnimation);

                //establecer la tercer pestaña como la seleccion actual
                selectedTab = 3;
            }
        });
    }

    public void openExpedientesFragment() {
        Log.d("MenuActivity", "Abriendo Nuevo Expediente Fragment");
        com.example.consultorioquetsales10.ExpedientesFragment ExpedientesFragment = new com.example.consultorioquetsales10.ExpedientesFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, ExpedientesFragment)
                .addToBackStack(null)
                .commit();
    }

    public void openRecetasFragment() {
        Log.d("MenuActivity", "Abriendo Nuevo Expediente Fragment");
        RecetasFragment RecetasFragment = new RecetasFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, RecetasFragment)
                .addToBackStack(null)
                .commit();
    }

    public void openExpedientesYRecetasFragment() {
        Log.d("MenuActivity", "Abriendo Nuevo Expediente Fragment");
        ExpedientesyRecetasFragment ExpedientesyRecetasFragment = new ExpedientesyRecetasFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, ExpedientesyRecetasFragment)
                .addToBackStack(null)
                .commit();
    }


    public class ExpedientesyRecetasFragment extends Fragment {
        private Button btnExpediente;
        private Button BtnReceta;
        private Button buttonCancelar;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_expedientes_y_recetas, container, false);
            View view1 = inflater.inflate(layout.fragment_expedientes, container, false);


            btnExpediente = view.findViewById(R.id.btnExpedinetes);
            BtnReceta = view.findViewById(id.BtnNuevaReceta);
            buttonCancelar = view1.findViewById(R.id.buttonCancelar);

            // Listener para abrir el fragmento de expedientes
            btnExpediente.setOnClickListener(v -> {
                Log.d("ExpedientesFragment", "Botón Nuevo Expediente pulsado");
                if (getActivity() instanceof MenuActivity) {
                    ((MenuActivity) getActivity()).openExpedientesFragment();
                } else {
                    Log.e("ExpedientesFragment", "Actividad no es una instancia de MenuActivity");
                }
            });

            BtnReceta.setOnClickListener(v -> {
                Log.d("RecetaFragment", "Botón Nuevo Expediente pulsado");
                if (getActivity() instanceof MenuActivity) {
                    ((MenuActivity) getActivity()).openRecetasFragment();
                } else {
                    Log.e("RecetaFragment", "Actividad no es una instancia de MenuActivity");
                }
            });
            buttonCancelar.setOnClickListener(v -> {
                if (getActivity() instanceof MenuActivity) {
                    ((MenuActivity) getActivity()).openExpedientesYRecetasFragment();
                }
                requireActivity().getSupportFragmentManager().popBackStack();
                finish();
            });
            return view;
        }
    }




    public void openCalendarFragment() {
        CalendarFragment calendarFragment = new CalendarFragment();
        //calendarFragment.setTargetFragment(agendaFragment, 1); // Relacionar AgendaFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, calendarFragment)
                .addToBackStack(null)
                .commit();
    }


    public static class AgendaFragment extends Fragment {
        private LinearLayout Citas;
        private TextView txtNoCitas;
        private ImageView iconNoCitas;
        private Button Btn_Cita;
        public static final int REQUEST_CODE_DATE_TIME = 1;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_agenda, container, false);

            Citas = view.findViewById(R.id.Citas);
            txtNoCitas = view.findViewById(R.id.txtNoCitas);
            iconNoCitas = view.findViewById(R.id.icon_Nocitas);
            Btn_Cita = view.findViewById(R.id.Btn_Cita);

            checkCitasVisibility();

            Btn_Cita.setOnClickListener(v -> {
                if (getActivity() instanceof MenuActivity) {
                    ((MenuActivity) getActivity()).openCalendarFragment();
                }
            });

            requireActivity ().getSupportFragmentManager ().setFragmentResultListener ("citaRequest", this, (requestKey , result) -> {
                String Fecha = result.getString ("Fecha");
                String Hora = result.getString ("Hora");
                addCita(Fecha, Hora);
            });

            return view;
        }

        public void addCita(String fecha, String hora) {
            View citaView = LayoutInflater.from (getContext ()).inflate (R.layout.fragment_cita , null , false);

            int Idunico = View.generateViewId ();
            citaView.setId (Idunico);

            TextView CitaTitulo = citaView.findViewById (R.id.CitaTitulo);
            TextView CitaFecha = citaView.findViewById (R.id.CitaFecha);
            TextView CitaHora = citaView.findViewById (id.CitaHora);
            ImageView BtnEliminar = citaView.findViewById (id.Btn_Eliminar);

            CitaTitulo.setText ("Cita Médica");
            CitaFecha.setText (fecha);
            CitaHora.setText (hora);

            BtnEliminar.setOnClickListener (v -> {
                Animation anim = AnimationUtils.loadAnimation (getContext () , R.anim.fragment_izquierda);
                citaView.startAnimation (anim);

                anim.setAnimationListener (new Animation.AnimationListener () {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Citas.removeView (citaView);
                        checkCitasVisibility ();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            });

            Citas.addView (citaView);
            checkCitasVisibility ();
        }

        public void checkCitasVisibility() {
            if (Citas.getChildCount () == 0) {
                txtNoCitas.setVisibility (View.VISIBLE);
                iconNoCitas.setVisibility (View.VISIBLE);
            } else {
                txtNoCitas.setVisibility (View.GONE);
                iconNoCitas.setVisibility (View.GONE);
            }
        }
    }

        /*@Override
        public void onActivityResult(int Solicitud , int Resultado , @Nullable Intent data) {
            super.onActivityResult(Solicitud, Resultado, data);
            if (Solicitud == REQUEST_CODE_DATE_TIME && Resultado == RESULT_OK && data != null) {
                String Fecha = data.getStringExtra ("Fecha");
                String Hora = data.getStringExtra ("Hora");
                View citaView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_cita , Citas , false);
                TextView CitaTitulo = citaView.findViewById (R.id.CitaTitulo);
                TextView CitaHora = citaView.findViewById (R.id.CitaHora);
                ImageView BtnEliminar = citaView.findViewById (R.id.Btn_Eliminar);
                CitaTitulo.setText ("Cita Médica");
                CitaHora.setText (Hora);

                BtnEliminar.setOnClickListener (v -> {
                    Animation anim = AnimationUtils.loadAnimation (getContext() , R.anim.fragment_izquierda);
                    citaView.startAnimation (anim);

                    anim.setAnimationListener (new Animation.AnimationListener () {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Citas.removeView (citaView);
                            checkCitasVisibility ();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                });

                Citas.addView(citaView);
                checkCitasVisibility ();
            }
        }

        public void checkCitasVisibility() {
            if (Citas.getChildCount() == 0) {
                txtNoCitas.setVisibility(View.VISIBLE);
                iconNoCitas.setVisibility(View.VISIBLE);
            } else {
                txtNoCitas.setVisibility(View.GONE);
                iconNoCitas.setVisibility(View.GONE);
            }
        }
    }*/

    // Clase CalendarFragment estática anidada
    public static class CalendarFragment extends Fragment {

        private DatePicker datePicker;
        private TimePicker timePicker;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate (R.layout.fragment_calendar , container , false);

            // Configuración del fragmento
            datePicker = view.findViewById(R.id.datePicker);
            timePicker = view.findViewById(R.id.timePicker);
            Button BtnOk = view.findViewById (R.id.Btn_Ok);
            Button BtnCancelar = view.findViewById (R.id.Btn_Cancelar);

            BtnOk.setOnClickListener(v -> {
                int Año = datePicker.getYear();
                int Mes = datePicker.getMonth() + 1;
                int Dia = datePicker.getDayOfMonth();
                int Hour = timePicker.getHour();
                int Minuto = timePicker.getMinute();

                String Fecha = Dia + "-" + Mes + "-" + Año;
                String Hora = String.format("%02d:%02d", Hour, Minuto);

                // Pasar los datos al fragmento objetivo
                Bundle result = new Bundle();
                result.putString("Fecha", Fecha);
                result.putString("Hora", Hora);

                requireActivity ().getSupportFragmentManager ().setFragmentResult ("citaRequest", result);

                requireActivity ().getSupportFragmentManager ().popBackStack ();

                /*if (getTargetFragment() != null) {
                    Intent intent = new Intent();
                    intent.putExtra("Fecha", Fecha);
                    intent.putExtra("Hora", Hora);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 0, intent);
                }*/

                //requireActivity ().getSupportFragmentManager ().popBackStack ();
            });

            BtnCancelar.setOnClickListener (v -> {
                // Volver sin guardar datos
                requireActivity ().getSupportFragmentManager ().popBackStack ();
            });

            return view;
        }
    }
}