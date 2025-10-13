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

public class reg_tutor extends AppCompatActivity {
    boolean gpass = false;
    boolean gphone = false;
    boolean gdeg = false;
    boolean gcourse = false;
    boolean gemail = false;
    boolean gfirst = false;
    boolean glast = false;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reg_tutor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnRegTutor = findViewById(R.id.btnRegTutor);
        btnRegTutor.setOnClickListener(v -> {
            EditText txtFirstTutor = (EditText) findViewById(R.id.txtFirstTutor);
            EditText txtLastTutor = (EditText) findViewById(R.id.txtLastTutor);
            EditText txtEmailTutor = (EditText) findViewById(R.id.txtEmailTutor);
            EditText txtRegPassTutor = (EditText) findViewById(R.id.txtRegPassTutor);
            EditText txtPhoneTutor = (EditText) findViewById(R.id.txtPhoneTutor);
            EditText txtDeg = (EditText) findViewById(R.id.txtDeg);
            EditText txtCourseOffered = (EditText) findViewById(R.id.txtCourseOffered);
            if(txtFirstTutor.getText().toString().trim().length()==0){
                Toast.makeText(this, "Invalid first name", Toast.LENGTH_SHORT).show();
                gfirst = false;


            }else{

                gfirst = true;
            }
            if(txtLastTutor.getText().toString().trim().length()==0){
                Toast.makeText(this, "Invalid last name", Toast.LENGTH_SHORT).show();
                glast = false;


            }else{
                glast = true;
            }
            if(txtEmailTutor.getText().toString().trim().contains("@")){
                gemail = true;

            }
            else{
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
                gemail = false;

            }
            if(txtRegPassTutor.getText().toString().trim().length()<8){
                Toast.makeText(this, "Invalid password. Must be at least 8 characters", Toast.LENGTH_SHORT).show();
                gpass = false;

            }else {
                gpass = true;
            }
            if(txtPhoneTutor.getText().toString().trim().length()!=10){
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                gphone = false;

            }else{
                gphone = true;
            }
            if(txtDeg.getText().toString().trim().length()==0){
                Toast.makeText(this, "Invalid degree", Toast.LENGTH_SHORT).show();
                gdeg = false;


            }else {
                gdeg = true;
            }
            if(txtCourseOffered.getText().toString().trim().length()==0){
                Toast.makeText(this, "Invalid course selection", Toast.LENGTH_SHORT).show();
                gcourse = false;



            }else{
                String g = txtCourseOffered.getText().toString().trim();
                gcourse = true;
            }

            if(gfirst && glast && gemail && gpass && gphone && gdeg && gcourse){
                String a = txtFirstTutor.getText().toString().trim();
                String b = txtLastTutor.getText().toString().trim();
                String c = txtEmailTutor.getText().toString().trim();
                String d = txtRegPassTutor.getText().toString().trim();
                String e = txtPhoneTutor.getText().toString().trim();
                String f = txtDeg.getText().toString().trim();
                MainActivity.tutor newTutor = new MainActivity.tutor(a, b, c, d, e, f);
                MainActivity.tutorAccounts.add(c);
                MainActivity.tutors.add(newTutor);
                Intent intent = new Intent(reg_tutor.this, MainActivity.class);
                startActivity(intent);
            }
        });
        }
    }
