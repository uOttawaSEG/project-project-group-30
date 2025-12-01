package com.example.project;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class see_sessions extends AppCompatActivity {
    private Button btnBack;
    EditText searchBar;
    ArrayAdapter<String> adapter;

    ListView listView;
    ArrayList<String> sessions;
    DatabaseReference studentsessions,accountsTutorRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_sessions);
        btnBack = findViewById(R.id.btnBack);
        sessions = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sessions);

        studentsessions = FirebaseDatabase.getInstance().getReference("Dates");
        accountsTutorRef = FirebaseDatabase.getInstance().getReference("Accounts");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUid();


        loadMySessions();
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(see_sessions.this, welcome_student.class);
            startActivity(intent);
        });
    }
        private void loadMySessions() {

            studentsessions.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String istaken = ds.child("IsTaken").getValue(String.class);
                        String studentId = ds.child("Student").getValue(String.class);
                        if (studentId != null && studentId.equals(userId) && !"no".equalsIgnoreCase(istaken)) {
                            String id = ds.child("Student").getValue(String.class);
                            if (userId.equals(id)) {
                                String tutorId = ds.child("Tutor").getValue(String.class);
                                String date = ds.child("Date").getValue(String.class);
                                String start = ds.child("Start").getValue(String.class);
                                String end = ds.child("End").getValue(String.class);
                                String course = ds.child("Course Code").getValue(String.class);
                                String status = ds.child("IsTaken").getValue(String.class);
                                accountsTutorRef.child(tutorId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot tutorSnap) {
                                        String first = tutorSnap.child("First").getValue(String.class);
                                        String last = tutorSnap.child("Last").getValue(String.class);
                                        Integer rating = tutorSnap.child("Rating").getValue(Integer.class);

                                        if (rating == null) rating = 0;
                                        String tutorName = "";
                                        if (first != null) tutorName += first;
                                        if (last != null) tutorName += " " + last;
                                        if (tutorName.trim().isEmpty()) tutorName = tutorId; // fallback

                                        String slotInfo = tutorName + " — " + course + "\n" + date + " | " + start + " - " + end + " | " + rating + " star Rating | Status of Request: " + status;

                                        sessions.add(slotInfo);
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                        }
                    }
                    if (sessions.isEmpty()) {
                        Toast.makeText(see_sessions.this, "No available sessions found.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(see_sessions.this, "Failed to load data.", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

