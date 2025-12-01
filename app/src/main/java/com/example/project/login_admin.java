package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login_admin extends AppCompatActivity {
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnContinueAdmin;
    private Button adbackbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // tells the system to display this xml file as the ui
        setContentView(R.layout.activity_login_admin);
// assign the characteristics of the actual button to the varible declared in this code, same as the username and password text fields
        btnContinueAdmin = findViewById(R.id.btnContinueAdmin);
        adbackbutton = findViewById(R.id.adbackbutton);
        txtUsername =(EditText) findViewById(R.id.txtUserAdmin);
        txtPassword =(EditText) findViewById(R.id.txtPassAdmin);
// when the button is clicked it makes sure that the text in the user is the same as the admin login and the [password is the same as the admin
        btnContinueAdmin.setOnClickListener(v -> {
            String email = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            // if its the same it takes them to the admin welcome page
           if(email.equals("admin@uottawa.ca") && password.equals("Admin101")){
               Intent intent = new Intent(login_admin.this, welcome_admin.class);
               startActivity(intent);
            }
           // if its different then it sends a message that its either the wrong email or password
           else{
               Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
           }
        });
        adbackbutton.setOnClickListener(v -> {
            Intent intent = new Intent(login_admin.this, login.class);
            startActivity(intent);
        });

    }
}
