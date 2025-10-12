package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class reg extends AppCompatActivity {
    private Button btnStudent;
    private Button btnTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        btnStudent = findViewById(R.id.btnStudentRegister);
        btnTutor = findViewById(R.id.btnTutorRegister);

        btnStudent.setOnClickListener(v -> {
            Intent intent = new Intent(reg.this, reg_student.class);
            startActivity(intent);
        });
        btnTutor.setOnClickListener(v -> {
            Intent intent = new Intent(reg.this, reg_tutor.class);
            startActivity(intent);
        });


    }
}