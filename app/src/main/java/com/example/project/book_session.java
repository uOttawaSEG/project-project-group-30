            package com.example.project;

            import android.content.Intent;
            import android.os.Bundle;
            import android.text.Editable;
            import android.text.TextWatcher;
            import android.view.View;
            import android.widget.ArrayAdapter;
            import android.widget.Button;
            import android.widget.EditText;
            import android.widget.ListView;
            import android.widget.Toast;
            import android.app.AlertDialog;

            import androidx.annotation.NonNull;

            import com.google.android.material.search.SearchBar;
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
                ArrayList<String> courseList; //store only the course codes
                ArrayList<String> displayedKeys;
                Button btnBack;

                Boolean dataLoaded = false;


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
                    courseList = new ArrayList<>();
                    displayedKeys = new ArrayList<>();
                    mAuth = FirebaseAuth.getInstance();
                    user = mAuth.getCurrentUser();
                    userId = user.getUid();


                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
                    listView.setAdapter(adapter);
                    datesRef = FirebaseDatabase.getInstance().getReference("Dates");
                    accountsStudentRef = FirebaseDatabase.getInstance().getReference("Accounts").child(userId);
                    accountsTutorRef = FirebaseDatabase.getInstance().getReference("Accounts");

                    loadAvailableSessions();
                    listView.setVisibility(View.INVISIBLE);
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
                            listView.setVisibility(View.VISIBLE);
                            if (!dataLoaded) return;
                            adapter.clear();
                            displayedKeys.clear();

                            if(s.toString().trim().isEmpty()){
                                listView.setVisibility(View.VISIBLE);
                            }
                            else {
                                for (int i = 0; i < courseList.size(); i++) {
                                    String code = courseList.get(i);
                                    if (code!=null) {
                                        //removes spaces and uppercase
                                        if(courseList.get(i).toLowerCase().replace(" ","").equals(s.toString().toLowerCase().replace(" ",""))) {
                                            adapter.add(slotList.get(i));
                                            displayedKeys.add(slotKeys.get(i));
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();

                            }//only shows sessions when text matches the course code
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        String selectedItem = adapter.getItem(position);
                        int originalIndex ;  //position in the list that stores all info
                        if(searchBar.getText().toString().isEmpty()){
                            originalIndex=slotList.indexOf(selectedItem);
                        }
                        else {
                            if(position <displayedKeys.size()) {
                                originalIndex = slotKeys.indexOf(displayedKeys.get(position));
                            }
                            else {
                                return;
                            }
                        }
                        if(originalIndex == -1) return;
                        String selectedKey = slotKeys.get(originalIndex);
                        new AlertDialog.Builder(this)
                                .setTitle("Request Session")
                                .setMessage("Do you want to request this session?\n\n" + selectedItem)
                                .setPositiveButton("Request", (dialog, which) -> {
                                    String date = selectedItem.split("\n")[1].split(" \\| ")[0];
                                    String start = selectedItem.split("\\|")[1].trim().split(" - ")[0];
                                    String end = selectedItem.split("\\|")[1].trim().split(" - ")[1];

                                    int newStartMin = Integer.parseInt(start.substring(0,2)) * 60 + Integer.parseInt(start.substring(2,4));
                                    int newEndMin   = Integer.parseInt(end.substring(0,2)) * 60 + Integer.parseInt(end.substring(2,4));
                                    datesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Boolean autoAcceptStatus = snapshot.child(selectedKey).child("AutoAccept").getValue(Boolean.class);
                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                String studentId = ds.child("Student").getValue(String.class);
                                                String status = ds.child("IsTaken").getValue(String.class);

                                                if (studentId != null && studentId.equals(userId) && !"no".equals(status)) {

                                                    String existingDate = ds.child("Date").getValue(String.class);
                                                    if (!date.equals(existingDate)) continue;

                                                    String s = ds.child("Start").getValue(String.class);
                                                    String e = ds.child("End").getValue(String.class);

                                                    int existStartMin = Integer.parseInt(s.substring(0, 2)) * 60 + Integer.parseInt(s.substring(2, 4));
                                                    int existEndMin = Integer.parseInt(e.substring(0, 2)) * 60 + Integer.parseInt(e.substring(2, 4));

                                                    if (newStartMin < existEndMin && newEndMin > existStartMin) {
                                                        Toast.makeText(book_session.this, "You already have a session that overlaps with this time.", Toast.LENGTH_LONG).show();
                                                        return;
                                                    }
                                                }
                                            }
                                            Map<String, Object> update = new HashMap<>();
                                            if(autoAcceptStatus){
                                                update.put("IsTaken", "yes");
                                            }
                                            else {
                                                update.put("IsTaken", "pending");
                                            }
                                            update.put("Student", userId);
                                            update.put("StudentName", First_name + " " + Last_name);
                                            datesRef.child(selectedKey).updateChildren(update);
                                            Toast.makeText(book_session.this, "Session requested", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(book_session.this, welcome_student.class);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                        });

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
                                courseList.clear();
                                displayedKeys.clear();

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

                                            String slotInfo = course + " — " + tutorName + "\n" + date + " | " + start + " - " + end + " | " + rating + " star Rating";

                                            slotList.add(slotInfo);
                                            slotKeys.add(ds.getKey());
                                            courseList.add(course);
                                            adapter.add(slotInfo);
                                            adapter.notifyDataSetChanged();
                                            dataLoaded = true;
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }

                                if (slotList.isEmpty() && listView.getVisibility()==View.VISIBLE) {
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
