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
        // assign the characteristics of the actual button to the varible declared in this code, same as the username and password text fields
        btnContinueTutor = findViewById(R.id.btnContinueTutor);
        EditText txtUsername = (EditText) findViewById(R.id.txtUserTutor);
        EditText txtPassword =(EditText) findViewById(R.id.txtPassTutor);
        // when the button is clicked it makes sure that the text in the user is the same as the tutor login and the password, first it goes through all the tutors in the tutor list and it calls a function to get the tutor password and tutor user and then verifies, if both of them dont match it goes onto the next in the list
        btnContinueTutor.setOnClickListener(v -> {
            String email = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            for(MainActivity.tutor t :MainActivity.tutors){
//if both the user and password are correct it takes us to the the tutor login page
                if(email.equals(t.getEmail()) && password.equals(t.getPassword())) {
                 Intent intent = new Intent(login_tutor.this, welcome_tutor.class);
                 startActivity(intent);
                 return;
            }
            }
            //if it goes through the for loop and doest take them to the next page then that means that theres no tutors in the tutor list that match these login details therefore it prints an message saying its the wrong password or user
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
        });

    }
}