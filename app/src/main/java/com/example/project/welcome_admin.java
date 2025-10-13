package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class welcome_admin extends AppCompatActivity {
    // Button that allows the admin to log out and return to the main screen
    // button for logging out
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the layout of this screen to activity_welcome_admin.xml
        setContentView(R.layout.activity_welcome_admin);
        // connect the logout button variable to the actual button in the XML layout
        btnLogout = findViewById(R.id.btnLogout);
        // when the logout button is clicked, return the user to the main screen
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(welcome_admin.this, MainActivity.class);
            startActivity(intent);
        });
    }
}