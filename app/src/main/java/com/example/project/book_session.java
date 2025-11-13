package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import java.util.HashMap;
import java.util.Map;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class book_session extends AppCompatActivity {
    EditText searchBar;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> slotList;
    ArrayList<String> slotKeys;
    Button btnBack;

    DatabaseReference datesRef;
    DatabaseReference accountsStudentRef;
    DatabaseReference accountsTutorRef;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String First_name;
    String Last_name;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksession);

        searchBar = findViewById(R.id.searchBar);
        listView = findViewById(R.id.listSessions);
        btnBack = findViewById(R.id.btnBack);
        slotList = new ArrayList<>();
        slotKeys = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, slotList);
        listView.setAdapter(adapter);
        datesRef = FirebaseDatabase.getInstance().getReference("Dates");
        accountsStudentRef = FirebaseDatabase.getInstance().getReference("Accounts").child(userId);
        accountsTutorRef = FirebaseDatabase.getInstance().getReference("Accounts");

        loadAvailableSessions();

        accountsStudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    First_name = snapshot.child("First").getValue(String.class);
                    Last_name = snapshot.child("Last").getValue(String.class);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = adapter.getItem(position);
            String selectedKey = slotKeys.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Request Session")
                    .setMessage("Do you want to request this session?\n\n" + selectedItem)
                    .setPositiveButton("Request", (dialog, which) -> {
                        Map<String, Object> update = new HashMap<>();
                        update.put("IsTaken", "pending");
                        update.put("Student", userId);
                        update.put("StudentName", First_name + " " + Last_name);
                        datesRef.child(selectedKey).updateChildren(update);
                        Toast.makeText(this, "Session requested!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(book_session.this, welcome_student.class);
            startActivity(intent);
        });
    }

        private void loadAvailableSessions () {
            datesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    slotList.clear();
                    slotKeys.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String isTaken = ds.child("IsTaken").getValue(String.class);
                        if (!"no".equalsIgnoreCase(isTaken)) continue;

                        String tutorId = ds.child("Tutor").getValue(String.class);
                        String date = ds.child("Date").getValue(String.class);
                        String start = ds.child("Start").getValue(String.class);
                        String end = ds.child("End").getValue(String.class);
                        String course = ds.child("Course Code").getValue(String.class);

                        if (tutorId == null) continue;


                        accountsTutorRef.child(tutorId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot tutorSnap) {
                                String first = tutorSnap.child("First").getValue(String.class);
                                String last = tutorSnap.child("Last").getValue(String.class);
                                Integer rating = tutorSnap.child("Rating").getValue(Integer.class);
                                String tutorName = "";
                                if (first != null) tutorName += first;
                                if (last != null) tutorName += " " + last;
                                if (tutorName.trim().isEmpty()) tutorName = tutorId; // fallback

                                String slotInfo = tutorName + " — " + course + "\n" + date + " | " + start + " - " + end + " | " + rating;

                                slotList.add(slotInfo);
                                slotKeys.add(ds.getKey());

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }

                    if (slotList.isEmpty()) {
                        Toast.makeText(book_session.this, "No available sessions found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(book_session.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
