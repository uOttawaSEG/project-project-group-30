package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login_tutor extends AppCompatActivity {

    private Button btnContinueTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_tutor);

        btnContinueTutor = findViewById(R.id.btnContinueTutor);
        EditText txtUsername = (EditText) findViewById(R.id.txtUserTutor);
        EditText txtPassword =(EditText) findViewById(R.id.txtPassTutor);
        btnContinueTutor.setOnClickListener(v -> {
            String email = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            for(MainActivity.tutor t :MainActivity.tutors){
                if(email.equals(t.getEmail()) && password.equals(t.getPassword())) {
                 Intent intent = new Intent(login_tutor.this, welcome_tutor.class);
                 startActivity(intent);
                 return;
            }
            }
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
        });

    }
}