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
    //EditText searchBar;
    private ArrayAdapter<String> adapter;

    private ListView listView;
    private ArrayList<String> sessions;
    private ArrayList<String>  sessionIds;
    private DatabaseReference datesRef,accountsRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_sessions);

        btnBack = findViewById(R.id.btnBack);
        listView = findViewById(R.id.listSessions);
        sessions = new ArrayList<>();
        sessionIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sessions);
        listView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUid();

        datesRef = FirebaseDatabase.getInstance().getReference("Dates");
        accountsRef = FirebaseDatabase.getInstance().getReference("Accounts");



        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(see_sessions.this, welcome_student.class);
            startActivity(intent);
        });

        datesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sessions.clear();
                sessionIds.clear();
                boolean hasSlots = false;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String taken = ds.child("IsTaken").getValue(String.class);
                    String student = ds.child("Student").getValue(String.class);
                    if(student == null || taken == null){
                        continue;
                    }
                    if(student.equals(userId) && !taken.equalsIgnoreCase("no")){
                        hasSlots = true;
                        String slotId = ds.getKey();
                        String raiting =ds.child("Raiting").getValue(String.class);
                        String tutorId = ds.child("Tutor").getValue(String.class);
                        String date = ds.child("Date").getValue(String.class);
                        String start = ds.child("Start").getValue(String.class);
                        String end = ds.child("End").getValue(String.class);
                        String course = ds.child("Course Code").getValue(String.class);

                        loadTutorName(tutorId, name ->{
                            String info = name + " - " + course + "\n" + date + " | " + start + " - "+ end + " | Approval: " + taken;
                            sessions.add(info);
                            adapter.notifyDataSetChanged();
                        });
                    }
                }
                if (!hasSlots) {
                    Toast.makeText(see_sessions.this, "You have no booked sessions", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(see_sessions.this, "Failed to load data.", Toast.LENGTH_SHORT).show();

            }
        });
    }
        private void loadTutorName(String tutorId, OnTutorLoaded listener) {
            accountsRef.child(tutorId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snap) {
                    String first = snap.child("First").getValue(String.class);
                    String last = snap.child("Last").getValue(String.class);

                    String name = (first != null ? first : "") + " " +
                            (last != null ? last : "");

                    if (name.trim().isEmpty())
                        name = "Tutor " + tutorId;

                    listener.onLoaded(name);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
    interface OnTutorLoaded {
        void onLoaded(String tutorName);
    }

}




