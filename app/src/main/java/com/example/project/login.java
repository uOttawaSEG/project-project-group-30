package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {
// button for logging into admin
    private Button btnLoginAdmin;
    // button for logging in as a student
    private Button btnLoginStudent;
    //button for logging in as a tutor
    private Button btnLoginTutor;
    private Button lbackbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLoginAdmin = findViewById(R.id.btnLoginAdmin);
        btnLoginStudent = findViewById(R.id.btnLoginStudent);
        btnLoginTutor = findViewById(R.id.btnLoginTutor);
        lbackbutton = findViewById(R.id.lbackbutton);

// when the button to log in as admin is clicked it makes an intent which tells the system to go from this screen to the admin login class
        btnLoginAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, login_admin.class);
            startActivity(intent);
        });
        // likewise but with the student login
        btnLoginStudent.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, login_student.class);
            startActivity(intent);
        });
        // also the same but with the tutor login
        btnLoginTutor.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, login_tutor.class);
            startActivity(intent);
        });
        lbackbutton.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
        });


    }
}