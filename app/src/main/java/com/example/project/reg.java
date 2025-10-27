package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class reg extends AppCompatActivity {
    // buttons for choosing whether to register as a student or a tutor
    private Button btnStudent;
    private Button btnTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the layout of this screen to activity_reg.xml
        setContentView(R.layout.activity_reg);
        // connect the button variables to the actual buttons in the XML layout
        btnStudent = findViewById(R.id.btnStudentRegister);
        btnTutor = findViewById(R.id.btnTutorRegister);
        // when the student button is clicked, open the student registration screen
        btnStudent.setOnClickListener(v -> {
            Intent intent = new Intent(reg.this, reg_student.class);
            startActivity(intent);
        });
        // when the tutor button is clicked, open the tutor registration screen
        btnTutor.setOnClickListener(v -> {
            Intent intent = new Intent(reg.this, reg_tutor.class);
            startActivity(intent);
        });


    }
}