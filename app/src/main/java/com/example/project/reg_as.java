package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class reg_as extends AppCompatActivity {
    private Button btnStudent;
    private Button btnTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_as);

        btnStudent = findViewById(R.id.btnStudentRegister);
        btnTutor = findViewById(R.id.btnTutorRegister);

        btnStudent.setOnClickListener(v -> {
            Intent intent = new Intent(reg_as.this, reg_student.class);
            startActivity(intent);
        });
        btnTutor.setOnClickListener(v -> {
            Intent intent = new Intent(reg_as.this, reg_tutor.class);
            startActivity(intent);
        });


    }
}