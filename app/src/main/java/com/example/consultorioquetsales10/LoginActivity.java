package com.example.consultorioquetsales10;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    EditText login_username;
    EditText login_password;
    Button login_button;
    TextView Bienvenido, dialog_language, español, Usuario, Contraseña, Login_Button, Huella;
    ImageButton btnIdioma;
    int lang_selected;
    RelativeLayout show_lan_dialog;
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);

        Bienvenido = findViewById (R.id.Bienvenido);
        Usuario = findViewById (R.id.login_username);
        Contraseña = findViewById (R.id.login_password);
        Login_Button = findViewById (R.id.login_button);
        Huella = findViewById (R.id.huella_button);
        dialog_language = findViewById (R.id.dialog_language);
        show_lan_dialog = findViewById (R.id.showlangdialog);

        if (LocaleHelper.getLanguage (LoginActivity.this).equalsIgnoreCase ("es"))
        {
            context = LocaleHelper.setLocale (LoginActivity.this, "es");
            resources = context.getResources ();

            lang_selected = 0;
            dialog_language.setText ("Español");
            Bienvenido.setText (resources.getString (R.string.title_welcome));

        }else if(LocaleHelper.getLanguage (LoginActivity.this).equalsIgnoreCase ("en"))
        {
            context = LocaleHelper.setLocale (LoginActivity.this, "en");
            resources = context.getResources ();

            lang_selected = 1;
            dialog_language.setText ("English");
            Bienvenido.setText (resources.getString (R.string.title_welcome));
        }

        show_lan_dialog.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                final String[] Language = {"Español", "English"};

                final AlertDialog.Builder builder = new AlertDialog.Builder (LoginActivity.this);

                builder.setTitle ("Selecciona un Idioma")
                        .setSingleChoiceItems (Language , lang_selected , new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialogInterface , int i) {
                                dialog_language.setText (Language[i]);

                                if(Language[i].equals ("Español"))
                                {
                                    context = LocaleHelper.setLocale (LoginActivity.this, "es");
                                    resources = context.getResources ();

                                    lang_selected = 0;

                                    Bienvenido.setText (resources.getString (R.string.title_welcome));
                                    Login_Button.setText (resources.getString (R.string.login_button));
                                    Huella.setText (resources.getString (R.string.huella_button));
                                }

                                if(Language[i].equals ("English"))
                                {
                                    context = LocaleHelper.setLocale (LoginActivity.this, "en");
                                    resources = context.getResources ();

                                    lang_selected = 1;

                                    Bienvenido.setText (resources.getString (R.string.welcome));

                                    Login_Button.setText (resources.getString (R.string.login_button));
                                    Huella.setText (resources.getString (R.string.huella_button));
                                }
                            }
                        })
                        .setPositiveButton ("Ok" , new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialogInterface , int i) {
                                dialogInterface.dismiss ();
                            }
                        });
                builder.create ().show ();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (!validateUsername() | !validatePassword()){

                }else{
                    if(login_username.getText().toString().trim().equals("Doctor") && login_password.getText().toString().trim().equals("12345")){
                        showSuccessDialog();
                    }else {
                        showDeniedDialog();
                    }
                }
            }
        });

        Button btnIngresar = findViewById(R.id.huella_button);
        btnIngresar.setOnClickListener(view -> showBiometricPrompt());
    }
    public Boolean validateUsername() {
        String val= login_username.getText().toString();
        if (val.isEmpty()){
            login_username.setError("El Username no debe estar vacío");
            return false;
        }else {
            login_username.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val = login_password.getText().toString();
        if (val.isEmpty()){
            login_password.setError("La contraseña no debe estar vacía");
            return false;
        }else{
            login_password.setError(null);
            return true;
        }
    }

    private void showSuccessDialog(){
        ConstraintLayout successConstraintLayout = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.success_dialog, successConstraintLayout);
        Button successDone = view.findViewById(R.id.successDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        successDone.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        if (alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showDeniedDialog(){
        ConstraintLayout deniedConstraintLayout = findViewById(R.id.deniedConstraintLayout);
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.login_denied_dialog, deniedConstraintLayout);
        Button deniedDone = view.findViewById(R.id.deniedDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        deniedDone.findViewById(R.id.deniedDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        if (alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);

        androidx.biometric.BiometricPrompt biometricPrompt = new androidx.biometric.BiometricPrompt(this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                showToast("Autenticación error: " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                showToast("Autenticación fallida: ");
            }
        });

        androidx.biometric.BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
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