package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class reg_student extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reg_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
        Button btnRegStudent = findViewById(R.id.btnRegStudent);
        btnRegStudent.setOnClickListener(v -> {
            EditText txtFirstTutor = (EditText) findViewById(R.id.txtFirstTutor);
            EditText txtLastTutor = (EditText) findViewById(R.id.txtLastTutor);
            EditText txtEmailTutor = (EditText) findViewById(R.id.txtEmailTutor);
            EditText txtRegPassTutor = (EditText) findViewById(R.id.txtRegPassTutor);
            EditText txtPhoneTutor = (EditText) findViewById(R.id.txtPhoneTutor);
            EditText txtProg = (EditText) findViewById(R.id.txtProg);
            String a = txtFirstTutor.getText().toString().trim();
            String b = txtLastTutor.getText().toString().trim();
            String c = txtEmailTutor.getText().toString().trim();
            String d = txtRegPassTutor.getText().toString().trim();
            String e = txtPhoneTutor.getText().toString().trim();
            String f = txtProg.getText().toString().trim();
            MainActivity.student newStudent = new MainActivity.student(a, b, c, d, e, f);
            MainActivity.studentAccounts.add(c);
            MainActivity.students.add(newStudent);
            Intent intent = new Intent(reg_student.this, MainActivity.class);
            startActivity(intent);
        });

    }

    }




