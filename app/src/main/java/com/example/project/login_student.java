package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login_student extends AppCompatActivity {

    private Button btnContinueStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnContinueStudent = findViewById(R.id.btnContinueStudent);
        EditText txtUsername = (EditText) findViewById(R.id.txtUserStudent);
        EditText txtPassword =(EditText) findViewById(R.id.txtPassStudent);
        btnContinueStudent.setOnClickListener(v -> {
            String email = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            for(MainActivity.student s :MainActivity.students){
                if(email.equals(s.getEmail()) && password.equals(s.getPassword())) {
                    Intent intent = new Intent(login_student.this, welcome_student.class);
                    startActivity(intent);
                    return;
                }
            }
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
        });

    }

}