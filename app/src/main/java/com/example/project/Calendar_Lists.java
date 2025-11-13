package com.example.project;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Calendar_Lists extends AppCompatActivity {
    private Button btnAddAvail;
    FirebaseAuth mAuth;
    DatabaseReference datesRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //btnAddAvail = findViewById(R.id.btnAddAvail);
        //ViewPaer
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();
        String userId = User.getUid();


            EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar_lists);
        LinearLayout layout = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        datesRef = FirebaseDatabase.getInstance().getReference("Dates");

        // Load all dates belonging to this tutor
        datesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                layout.removeAllViews();

                boolean hasSlots = false;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String tutorId = ds.child("Tutor").getValue(String.class);

                    // Only show slots created by this tutor
                    if (tutorId != null && tutorId.equals(userId)) {
                        hasSlots = true;

                        String date = ds.child("Date").getValue(String.class);
                        String Course_code= ds.child("Course Code").getValue(String.class);
                        String start = ds.child("Start").getValue(String.class);
                        String end = ds.child("End").getValue(String.class);
                        String slotId = ds.getKey();

                        // Display button for each slot
                        Button btn = new Button(Calendar_Lists.this);
                        btn.setText(Course_code+ " | " + date + " | " + start + " - " + end);
                        btn.setAllCaps(false);

                        // On click → ask to delete
                        btn.setOnClickListener(v -> {
                            new AlertDialog.Builder(Calendar_Lists.this)
                                    .setTitle("Delete Time Slot")
                                    .setMessage("Do you want to delete this time slot?\n" + Course_code + " | " + date + " | " + start + " - " + end)
                                    .setPositiveButton("Yes", (dialog, which) -> {
                                        datesRef.child(slotId).removeValue()
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(Calendar_Lists.this, "Time slot deleted.", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(Calendar_Lists.this, "Failed to delete slot.", Toast.LENGTH_SHORT).show();
                                                });
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        });

                        layout.addView(btn);
                    }
                }

                if (!hasSlots) {
                    Button noSlots = new Button(Calendar_Lists.this);
                    noSlots.setText("No available time slots.");
                    noSlots.setEnabled(false);
                    layout.addView(noSlots);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Calendar_Lists.this, "Failed to load slots.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

