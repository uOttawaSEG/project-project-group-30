package com.example.project;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RejectedFragment extends Fragment {
    private DatabaseReference accountsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ScrollView scrollView = new ScrollView(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        scrollView.addView(layout);

        accountsRef = FirebaseDatabase.getInstance().getReference("Accounts");

        accountsRef.orderByChild("Status").equalTo(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            TextView tv = new TextView(getContext());
                            tv.setText("No requests rejected.");
                            layout.addView(tv);
                            return;
                        }

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String email = ds.child("Email").getValue(String.class);
                            String first = ds.child("First").getValue(String.class);
                            String last = ds.child("Last").getValue(String.class);
                            String phone = ds.child("Phone").getValue(String.class);
                            String degree = ds.child("Degree").getValue(String.class);
                            String job = ds.child("Job").getValue(String.class);
                            String courses = ds.child("Courses").getValue(String.class);

                            Button btn = new Button(getContext());
                            btn.setText(first + " " + last);
                            btn.setAllCaps(false);

                            btn.setOnClickListener(v -> {
                                StringBuilder message = new StringBuilder();
                                message.append("Email: ").append(email).append("\n")
                                        .append("Phone: ").append(phone).append("\n")
                                        .append("Degree: ").append(degree).append("\n")
                                        .append("Job: ").append(job);

                                if ("Tutor".equalsIgnoreCase(job) && courses != null && !courses.isEmpty()) {
                                    message.append("\nCourses Offered: ").append(courses);
                                }

                                new AlertDialog.Builder(getContext())
                                        .setTitle(first + " " + last)
                                        .setMessage(message.toString())
                                        .setPositiveButton("Approve", (dialog, which) -> {
                                            ds.getRef().child("Status").setValue(2)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(getContext(), "Approved " + first + " " + last, Toast.LENGTH_SHORT).show();
                                                        refreshFragment();
                                                    });
                                        })
                                        .setNeutralButton("Cancel", null)
                                        .show()
                                ;

                            });

                            layout.addView(btn);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Failed to load requests.", Toast.LENGTH_SHORT).show();
                    }
                });

        return scrollView;
    }

    private void refreshFragment() {
        if (getParentFragmentManager() != null) {
            getParentFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }
}
