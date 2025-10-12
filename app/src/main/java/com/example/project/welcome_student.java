package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class welcome_student extends AppCompatActivity {

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_student);

        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(welcome_student.this, MainActivity.class);
            startActivity(intent);
        });
    }
}