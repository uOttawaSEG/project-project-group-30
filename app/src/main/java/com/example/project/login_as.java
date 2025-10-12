package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login_as extends AppCompatActivity {

    private Button btnLoginAdmin;
    private Button btnLoginStudent;
    private Button btnLoginTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_as);

        btnLoginAdmin = findViewById(R.id.btnLoginAdmin);
        btnLoginStudent = findViewById(R.id.btnLoginStudent);
        btnLoginTutor = findViewById(R.id.btnLoginTutor);


        btnLoginAdmin.setOnClickListener(v -> {
            // Create Intent to start SecondActivity
            Intent intent = new Intent(login_as.this, login_admin.class);
            // Start the activity
            startActivity(intent);
        });
        btnLoginStudent.setOnClickListener(v -> {
            // Create Intent to start SecondActivity
            Intent intent = new Intent(login_as.this, login_student.class);
            // Start the activity
            startActivity(intent);
        });
        btnLoginTutor.setOnClickListener(v -> {
            // Create Intent to start SecondActivity
            Intent intent = new Intent(login_as.this, login_tutor.class);
            // Start the activity
            startActivity(intent);
        });

    }
}