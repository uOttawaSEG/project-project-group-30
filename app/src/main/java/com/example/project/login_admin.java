package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class login_admin extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnContinueAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        btnContinueAdmin = findViewById(R.id.btnContinueAdmin);


        btnContinueAdmin.setOnClickListener(v -> {
            txtEmail =(EditText) findViewById(R.id.txtEmail);
            txtPassword =(EditText) findViewById(R.id.txtPassword);

            Intent intent = new Intent(login_admin.this, login_as.class);
            startActivity(intent);
        });

    }
}
