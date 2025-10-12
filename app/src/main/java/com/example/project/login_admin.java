package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class login_admin extends AppCompatActivity {
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnContinueAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        btnContinueAdmin = findViewById(R.id.btnContinueAdmin);
        txtUsername =(EditText) findViewById(R.id.txtUsername);
        txtPassword =(EditText) findViewById(R.id.txtPassword);

        btnContinueAdmin.setOnClickListener(v -> {
            String email = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
           if(email.equals("admin@uottawa.ca") && password.equals("Admin101")){
               Intent intent = new Intent(login_admin.this, login_as.class);
               startActivity(intent);
            }

        });

    }
}
