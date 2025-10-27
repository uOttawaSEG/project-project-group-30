package com.example.project;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class login_tutor extends AppCompatActivity {

    private Button btnContinueTutor;
    FirebaseAuth mAuth;

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
            /* for(MainActivity.tutor t :MainActivity.tutors){
//if both the user and password are correct it takes us to the the tutor login page
                if(email.equals(t.getEmail()) && password.equals(t.getPassword())) {
                 Intent intent = new Intent(login_tutor.this, welcome_tutor.class);
                 startActivity(intent);
                 return;
            }
            }
            //if it goes through the for loop and doest take them to the next page then that means that theres no tutors in the tutor list that match these login details therefore it prints an message saying its the wrong password or user
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();

                */
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser User = mAuth.getCurrentUser();
                                String userId = User.getUid();
                                FirebaseDatabase.getInstance().getReference("Accounts").child(userId).child("Status").get().addOnCompleteListener(statusTask->{
                                    if(statusTask.isSuccessful()){
                                        Long status = (Long)statusTask.getResult().getValue();
                                        if(status == 1){
                                            Toast.makeText(login_tutor.this, "Your Account has been Rejected by the Admin. If you think this is incorrect please contact 123-456-7890", Toast.LENGTH_LONG).show();
                                        }
                                        else if(status==2){
                                            Toast.makeText(login_tutor.this, "Your Account has been Approved. Welcome!",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(login_tutor.this, welcome_student.class);
                                            startActivity(intent);
                                        }
                                        else if(status==0){
                                            Toast.makeText(login_tutor.this, "Your account is pending approval",Toast.LENGTH_LONG ).show();
                                        }
                                    }

                                });

                            }
                            else{
                                Toast.makeText(login_tutor.this,"Authentication failed", Toast.LENGTH_LONG).show();
                            }

                        }

                    });
        });

    }

    }