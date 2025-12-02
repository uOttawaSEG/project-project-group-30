package com.example.project;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        Date curdate = new Date();
        int curHour = curdate.getHours();
        int curMin = curdate.getMinutes();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSlotId = sessionIds.get(position);
            datesRef.child(selectedSlotId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String startTime = snapshot.child("Start").getValue(String.class);
                    //the current date
                    SimpleDateFormat dateFormatyd = new SimpleDateFormat("yyyy/MM/dd");
                    //date of the session
                    String slectedDate = snapshot.child("Date").getValue(String.class);
                    String isRated = snapshot.child("isRated").getValue(String.class);
                    if (startTime == null || slectedDate == null || isRated == null) {
                        Toast.makeText(see_sessions.this, "Error: Session data is incomplete.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        if (dateFormatyd.parse(dateFormatyd.format(curdate)).after(dateFormatyd.parse(slectedDate)) || curHour * 60 + curMin > Integer.valueOf(startTime.substring(0, 2)) * 60 + Integer.valueOf(startTime.substring(2, 4)) && dateFormatyd.parse(dateFormatyd.format(curdate)).equals(dateFormatyd.parse(slectedDate))) {
                            if (snapshot.child("isRated").getValue(String.class).equals("yes")) {
                                Toast.makeText(see_sessions.this, "You have already rated this session", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(see_sessions.this);
                                builder.setTitle("Rate the tutor");
                                builder.setMessage("What would you rate this tutor out of 5?");
                                Context context = builder.getContext();
                                LinearLayout layout = new LinearLayout(context);
                                layout.setOrientation(LinearLayout.VERTICAL);

                                final EditText course_code = new EditText(context);
                                course_code.setHint("Course code");
                                course_code.setInputType(InputType.TYPE_CLASS_TEXT);
                                layout.addView(course_code);
                                builder.setView(layout);
                                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String stringEnteredRating = course_code.getText().toString();
                                        int enteredRating = Integer.parseInt(stringEnteredRating.trim());
                                        datesRef.child(selectedSlotId).child("isRated").setValue("yes");
                                        datesRef.child(selectedSlotId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                //gets the current raiting
                                                GenericTypeIndicator<List<Long>> t = new GenericTypeIndicator<List<Long>>() {};                                                List<Long> raitings = snapshot.child("NumberOfRaitings").getValue(t);
                                                if (raitings == null) {
                                                    raitings = new ArrayList<>();
                                                }
                                                raitings.add((long) enteredRating);
                                                int total = 0;
                                                for (Long rate : raitings) {
                                                    total += rate;
                                                }
                                                double averageRating = (double) total / raitings.size();
                                                //add it to the database
                                                java.util.Map<String, Object> updates = new java.util.HashMap<>();
                                                updates.put("NumberOfRaitings", raitings);
                                                updates.put("Rating", String.format("%.2f", averageRating));

                                                datesRef.child(selectedSlotId).updateChildren(updates);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });

                                        dialog.dismiss();
                                    }

                                });

                            }

                        } else {
                            AlertDialog.Builder mainBuilder = new AlertDialog.Builder(see_sessions.this);
                            mainBuilder.setTitle("Modify session");
                            mainBuilder.setMessage("Would you like to withdrawl from this session");
                            mainBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    datesRef.child(selectedSlotId).child("IsTaken").setValue("no");
                                    datesRef.child(selectedSlotId).child("StudentName").setValue("Empty");
                                    datesRef.child(selectedSlotId).child("Student").setValue("Empty");
                                    Toast.makeText(see_sessions.this, "You have succesfully withdrawn from this session " , Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(see_sessions.this, welcome_student.class);
                                    startActivity(intent);


                                }
                            });
                            mainBuilder.setNegativeButton("No", null);
                            mainBuilder.show();
                        }

                    } catch (ParseException e) {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(see_sessions.this, "Failed to read session details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
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
                    if (student == null || taken == null) {
                        continue;
                    }
                    if (student.equals(userId) && !taken.equalsIgnoreCase("no")) {
                        hasSlots = true;
                        String slotId = ds.getKey();

                        String raiting = ds.child("Rating").getValue(String.class);
                        String tutorId = ds.child("Tutor").getValue(String.class);
                        String date = ds.child("Date").getValue(String.class);
                        String start = ds.child("Start").getValue(String.class);
                        String end = ds.child("End").getValue(String.class);
                        String course = ds.child("Course Code").getValue(String.class);

                        loadTutorName(tutorId, name -> {
                            String info = name + " - " + course + "\n" + date + " | " + start + " - " + end + " | Approval: " + taken;
                            sessions.add(info);
                            sessionIds.add(slotId);
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




