package com.example.project;

import static android.content.ContentValues.TAG;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Objects;

public class reg_tutor extends AppCompatActivity {
    // boolean flags to track if input fields are valid
    boolean gpass = false;
    boolean gphone = false;
    boolean gdeg = false;
    boolean gcourse = false;
    boolean gemail = false;
    boolean gfirst = false;
    boolean glast = false;

    FirebaseAuth mAuth;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // enable EdgeToEdge display for full-screen layout
        EdgeToEdge.enable(this);
        // add padding to avoid overlapping with system bars
        setContentView(R.layout.activity_reg_tutor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // connect the register button from XML to code
        Button btnRegTutor = findViewById(R.id.btnRegTutor);
        // when the register button is clicked, validate input and create a new tutor
        btnRegTutor.setOnClickListener(v -> {
            // get text input fields from the layout
            EditText txtFirstTutor = (EditText) findViewById(R.id.txtFirstTutor);
            EditText txtLastTutor = (EditText) findViewById(R.id.txtLastTutor);
            EditText txtEmailTutor = (EditText) findViewById(R.id.txtEmailTutor);
            EditText txtRegPassTutor = (EditText) findViewById(R.id.txtRegPassTutor);
            EditText txtPhoneTutor = (EditText) findViewById(R.id.txtPhoneTutor);
            EditText txtDeg = (EditText) findViewById(R.id.txtDeg);
            EditText txtCourseOffered = (EditText) findViewById(R.id.txtCourseOffered);
            // validate first name
            if(txtFirstTutor.getText().toString().trim().length()==0){
                Toast.makeText(this, "Invalid first name", Toast.LENGTH_SHORT).show();
                gfirst = false;


            }else{

                gfirst = true;
            }
            // validate last name
            if(txtLastTutor.getText().toString().trim().length()==0){
                Toast.makeText(this, "Invalid last name", Toast.LENGTH_SHORT).show();
                glast = false;


            }else{
                glast = true;
            }
            // validate email contains '@'
            if(txtEmailTutor.getText().toString().trim().contains("@")){
                gemail = true;

            }
            else{
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
                gemail = false;

            }
            // validate password length
            if(txtRegPassTutor.getText().toString().trim().length()<8){
                Toast.makeText(this, "Invalid password. Must be at least 8 characters", Toast.LENGTH_SHORT).show();
                gpass = false;

            }else {
                gpass = true;
            }
            // validate password length
            if(txtPhoneTutor.getText().toString().trim().length()!=10){
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                gphone = false;

            }else{
                gphone = true;
            }
            // validate degree input
            if(txtDeg.getText().toString().trim().length()==0){
                Toast.makeText(this, "Invalid degree", Toast.LENGTH_SHORT).show();
                gdeg = false;


            }else {
                gdeg = true;
            }
            // validate courses offered
            if(txtCourseOffered.getText().toString().trim().length()==0){
                Toast.makeText(this, "Invalid course selection", Toast.LENGTH_SHORT).show();
                gcourse = false;



            }else{
                String g = txtCourseOffered.getText().toString().trim();
                gcourse = true;
            }
            // if all validations pass, create a new tutor account
            if(gfirst && glast && gemail && gpass && gphone && gdeg && gcourse){
                String a = txtFirstTutor.getText().toString().trim();
                String b = txtLastTutor.getText().toString().trim();
                String c = txtEmailTutor.getText().toString().trim();
                String d = txtRegPassTutor.getText().toString().trim();
                String e = txtPhoneTutor.getText().toString().trim();
                String f = txtDeg.getText().toString().trim();
                String g = txtCourseOffered.getText().toString().trim();
                // create new tutor object and add to global lists
                MainActivity.tutor newTutor = new MainActivity.tutor(a, b, c, d, e, f);
                MainActivity.tutorAccounts.add(c);
                MainActivity.tutors.add(newTutor);
                mAuth = FirebaseAuth.getInstance();
                String email = txtEmailTutor.getText().toString().trim();
                String password = txtRegPassTutor.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(reg_tutor.this, "Registration Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    // return to main activity after successful registration
                                    FirebaseUser user = mAuth.getCurrentUser();
                                        String userId = user.getUid();
                                    HashMap<String,Object> map = new HashMap<>();
                                    map.put("First", a);
                                    map.put("Last", b);
                                    map.put("Email", c);
                                    map.put("Phone", e);
                                    map.put("Degree", f);
                                    map.put("Courses" , g);
                                    map.put("UserId", userId);



                                    FirebaseDatabase.getInstance().getReference().child("Accounts").updateChildren(map);




                                    Intent intent = new Intent(reg_tutor.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(reg_tutor.this, "Registration failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                        });

            }
        });
        }
    }
