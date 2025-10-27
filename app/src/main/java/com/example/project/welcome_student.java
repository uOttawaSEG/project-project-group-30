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
    // Button that allows the admin to log out and return to the main screen


    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the UI layout for this screen to activity_welcome_admin.xml
        setContentView(R.layout.activity_welcome_student);
        // Link the button variable to the actual button in the layout
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            // Create an Intent to navigate from this screen back to MainActivity
            Intent intent = new Intent(welcome_student.this, MainActivity.class);
            startActivity(intent);
        });
    }
}