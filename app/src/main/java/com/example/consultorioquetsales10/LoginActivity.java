package com.example.consultorioquetsales10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText login_username;
    EditText login_password;
    Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
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
}