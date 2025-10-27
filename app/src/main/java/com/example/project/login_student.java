package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_student extends AppCompatActivity {

    private Button btnContinueStudent;
    FirebaseAuth mAuth;

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
        // assign the characteristics of the actual button to the varible declared in this code, same as the username and password text fields
        btnContinueStudent = findViewById(R.id.btnContinueStudent);
        EditText txtUsername = (EditText) findViewById(R.id.txtUserStudent);
        EditText txtPassword =(EditText) findViewById(R.id.txtPassStudent);
        // when the button is clicked it makes sure that the text in the user is the same as the student login and the password, it calles a function to get the student password and student user and then verifies
        btnContinueStudent.setOnClickListener(v -> {
            String email = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            /*for(MainActivity.student s :MainActivity.students){
                // if it matches up then it proceeds to student welcome page
                if(email.equals(s.getEmail()) && password.equals(s.getPassword())) {
                    Intent intent = new Intent(login_student.this, welcome_student.class);
                    startActivity(intent);
                    return;
                    //
                }
                // if its different then it sends a message that its either the wrong email or password
                else{
                    Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();

                }
            }*/
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(login_student.this, "Authentication Successful.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(login_student.this, welcome_student.class);
                                startActivity(intent);


                            } else {
                                Toast.makeText(login_student.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }

                    });
        });

    }

}