package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Calendar_Lists extends AppCompatActivity {
    private Button btnBack;
    private ListView listSessions;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private ArrayList<String> sessionList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> dataList = new ArrayList<>();

    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar_lists);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btnBack = findViewById(R.id.btnBack);
        listSessions = findViewById(R.id.listSessions);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();
        String userId = User.getUid();

        databaseRef = FirebaseDatabase.getInstance().getReference("Dates");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sessionList);
        listSessions.setAdapter(adapter);
        loadSessions(userId);

        listSessions.setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, String> sessions = dataList.get(position);
            String isTaken = sessions.get("IsTaken");
            String student = sessions.get("Student");

            AlertDialog.Builder dialog = new AlertDialog.Builder(Calendar_Lists.this);
            dialog.setTitle("Session Details");
            if(isTaken != null && isTaken.equals("yes") && student != null && !student.equals("Empty")){
                dialog.setMessage("StudentID: " + student);
            }
            else{
                dialog.setMessage("No student yet.");
            }
            dialog.setPositiveButton("OK", (d, which)-> d.dismiss());
            dialog.show();
        });

        btnBack.setOnClickListener(v->{
            Intent intent = new Intent(Calendar_Lists.this, welcome_tutor.class);
            startActivity(intent);
            finish();
        });


    }

    private void loadSessions(String userID){
        databaseRef.get().addOnSuccessListener(dataSnapshot ->{
            sessionList.clear();
            dataList.clear();

            for(DataSnapshot ds : dataSnapshot.getChildren()){
                String tutorID = ds.child("Tutor").getValue(String.class);
                if(tutorID != null && tutorID.equals(userID)){
                    String date = ds.child("Date").getValue(String.class);
                    String start = ds.child("Start").getValue(String.class);
                    String end = ds.child("End").getValue(String.class);
                    String isTaken = ds.child("IsTaken").getValue(String.class);
                    String student = ds.child("Student").getValue(String.class);

                    HashMap<String, String> sessionMap = new HashMap<>();
                    sessionMap.put("Date", date);
                    sessionMap.put("Start", start);
                    sessionMap.put("End", end);
                    sessionMap.put("IsTaken", isTaken);
                    sessionMap.put("Student", student);

                    dataList.add(sessionMap);

                    String status = (isTaken != null && isTaken.equals("yes")) ? "TAKEN" : "AVAILABLE";
                    sessionList.add(date + " | " + start + "-" + end + " -> " + status);
                }
            }
            adapter.notifyDataSetChanged();
            if(sessionList.isEmpty()){
                Toast.makeText(Calendar_Lists.this, "No sessions available.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(Calendar_Lists.this, "Failed to load data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }
}

