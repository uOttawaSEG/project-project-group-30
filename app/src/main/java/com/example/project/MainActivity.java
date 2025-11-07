package com.example.project;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //lists for students and tutors emails and passwords

    public static ArrayList<String> studentAccounts = new ArrayList<String>();
    public static ArrayList<String> tutorAccounts = new ArrayList<String>();
    //lists of all of the students and tutors
    static ArrayList<student> students = new ArrayList<student>();
    static ArrayList<tutor> tutors = new ArrayList<tutor>();
    public static ArrayList<String> requests = new ArrayList<String>();
    public static ArrayList<String> rejected = new ArrayList<String>();

    static String signedInAs = "";
    abstract static class user{
        user currentUser;
        String first;
        String last;
        String email;
        String password;
        String phone;
        String program;
        String highest;

        user(String first, String last, String email, String password, String phone, String program, String highest){
            this.first = first;
            this.last = last;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.program = program;
            this.highest = highest;
        }

        public String getFirst(){
            return first;
        }
        public user getCurrentUser(){
            return currentUser;
        }
        public String getLast(){
            return last;
        }
        public String getEmail(){
            return email;
        }

        public String getPassword(){
            return password;
        }
        public String getPhone(){
            return phone;
        }
        public String getProgram(){
            return program;
        }
        public String getHighest(){
            return highest;
        }

        public String getFullName() {
            return getFirst() + " " + getLast();
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        public void setProgram( String program){
            this.program=program;
        }
        public void setHighest(String highest){
            this.highest=highest;
        }
        public void setCurrentUser(user user){
            this.currentUser=user;
        }
    }

     static class student extends user{
        public student(String first, String last, String email, String password, String phone, String program) {
            super( first,  last,  email,  password,  phone,  program,  "");

        }
    }
    static class tutor extends user{
        public tutor(String first, String last, String email, String password, String phone, String highest) {
            super( first,  last,  email,  password,  phone, "", highest);
        }
    }



    // Declare Button object
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);



        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        });
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, reg.class);
            startActivity(intent);
        });
    }
}