package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class login_as extends AppCompatActivity {

    private Button btnLoginAdmin;
    private Button btnLoginStudent;
    private Button btnLoginTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_as);

        btnLoginAdmin = findViewById(R.id.btnLoginAdmin);
        btnLoginStudent = findViewById(R.id.btnLoginStudent);
        btnLoginTutor = findViewById(R.id.btnLoginTutor);


        btnLoginAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(login_as.this, login_admin.class);
            startActivity(intent);
        });
        btnLoginStudent.setOnClickListener(v -> {
            Intent intent = new Intent(login_as.this, login_student.class);
            startActivity(intent);
        });
        btnLoginTutor.setOnClickListener(v -> {
            Intent intent = new Intent(login_as.this, login_tutor.class);
            startActivity(intent);
        });

    }
}