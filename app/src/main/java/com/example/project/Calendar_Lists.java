package com.example.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                           //check if the date has already happened
                            Date curdate = new Date();
                            int curHour = curdate.getHours();
                            int curMin = curdate.getMinutes();
                            datesRef.child(slotId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String startTime=snapshot.child("Start").getValue(String.class);
                                    //the current date
                                    SimpleDateFormat dateFormatyd = new SimpleDateFormat("yyyy/MM/dd");
                                    //date of the session
                                    String slectedDate = snapshot.child("Date").getValue(String.class);

                                    try {
                                        //checks if the date is in the past or if on the current day the time has already happened
                                        if(dateFormatyd.parse(dateFormatyd.format(curdate)).after(dateFormatyd.parse(slectedDate)) || curHour*60+curMin > Integer.valueOf(startTime.substring(0,2))*60+Integer.valueOf(startTime.substring(2,4))&&dateFormatyd.parse(dateFormatyd.format(curdate)).equals(dateFormatyd.parse(slectedDate))){
                                            Toast.makeText(Calendar_Lists.this, "This time has already happened", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else {
                                            AlertDialog.Builder mainBuilder = new AlertDialog.Builder(Calendar_Lists.this);
                                            mainBuilder.setTitle("Modify session");
                                            mainBuilder.setMessage("Would you like to Delete the time slot or check approval status");
                                            mainBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    new AlertDialog.Builder(Calendar_Lists.this)
                                                            .setTitle("Delete Time Slot")
                                                            .setMessage("Do you want to delete this time slot?\n" + Course_code + " | " + date + " | " + start + " - " + end)
                                                            .setPositiveButton("Yes", (inDialog, inWhich) -> {
                                                                datesRef.child(slotId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if ("yes".equals(snapshot.child("IsTaken").getValue(String.class))) {
                                                                            Toast.makeText(Calendar_Lists.this, "A student is already signed up for this time slot. If you wish to delete it you must reject them first.", Toast.LENGTH_SHORT).show();
                                                                            return;
                                                                        }

                                                                        datesRef.child(slotId).removeValue()
                                                                                .addOnSuccessListener(aVoid -> {
                                                                                    Toast.makeText(Calendar_Lists.this, "Time slot deleted.", Toast.LENGTH_SHORT).show();
                                                                                })
                                                                                .addOnFailureListener(e -> {
                                                                                    Toast.makeText(Calendar_Lists.this, "Failed to delete slot.", Toast.LENGTH_SHORT).show();
                                                                                });
                                                                    }
                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                        Toast.makeText(Calendar_Lists.this, "Database read failed.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            })
                                                            .setNegativeButton("No", null)
                                                            .show();
                                                }
                                            });
                                            mainBuilder.setPositiveButton("Check Status", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    datesRef.child(slotId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        //check if someone is already signed up
                                                        //keeps track if someone is already registered,if no one is registred or if its pending with someone
                                                        int option=-1;
                                                        String message="";
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if ("yes".equals(snapshot.child("IsTaken").getValue(String.class))) {
                                                                message=snapshot.child("StudentName").getValue(String.class) + "is currently registered for this settion";
                                                                option=0;
                                                            } else if ("no".equals(snapshot.child("IsTaken").getValue(String.class))) {
                                                                message="no one is currently registered for this settion";
                                                                option=1;
                                                            } else if ("pending".equals(snapshot.child("IsTaken").getValue(String.class))) {
                                                                message=snapshot.child("StudentName").getValue(String.class) + "is currently requesting for this settion. Would you like to accept them?";
                                                                option=2;
                                                            }
                                                            AlertDialog.Builder statBuilder = new AlertDialog.Builder(Calendar_Lists.this);
                                                            statBuilder.setTitle("Current status");
                                                            statBuilder.setMessage(message);

                                                            if(option==1){
                                                                statBuilder.setPositiveButton("Ok", null);
                                                            } else if (option==0) {
                                                                statBuilder.setPositiveButton("Reject them", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        datesRef.child(slotId).child("IsTaken").setValue("no");
                                                                        datesRef.child(slotId).child("StudentName").setValue("Empty");
                                                                        datesRef.child(slotId).child("Student").setValue("Empty");
                                                                    }
                                                                });
                                                                statBuilder.setNegativeButton("Ok", null);
                                                            } else if (option==2) {
                                                                statBuilder.setPositiveButton("Reject them", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        datesRef.child(slotId).child("IsTaken").setValue("no");
                                                                        datesRef.child(slotId).child("StudentName").setValue("Empty");
                                                                        datesRef.child(slotId).child("Student").setValue("Empty");
                                                                    }
                                                                });
                                                                statBuilder.setNegativeButton("Accept them", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        datesRef.child(slotId).child("IsTaken").setValue("yes");
                                                                    }
                                                                });


                                                            }

                                                            statBuilder.show();

                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Toast.makeText(Calendar_Lists.this, "Failed to read status: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            });
                                            mainBuilder.show();
                                        }
                                    } catch (ParseException e) {
                                        return;

                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }

                            });



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

