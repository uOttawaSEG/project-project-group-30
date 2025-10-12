package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class reg_student extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static Button btnRegStudent;


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
            String a = String.valueOf(findViewById(R.id.txtFirstTutor));
            String b = String.valueOf(findViewById(R.id.txtLastTutor));
            String c = String.valueOf(findViewById(R.id.txtEmailTutor));
            String d = String.valueOf(findViewById(R.id.txtRegPassTutor));
            String e = String.valueOf(findViewById(R.id.txtPhoneTutor));
            String f = String.valueOf(findViewById(R.id.txtProg));
            MainActivity.student newStudent = new MainActivity.student(a, b, c, d, e, f);
            MainActivity.studentAccounts.add(c);
            MainActivity.students.add(newStudent);
            Intent intent = new Intent(reg_student.this, MainActivity.class);
            startActivity(intent);
        });

    }

    }




