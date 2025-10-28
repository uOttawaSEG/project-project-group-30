package com.example.project;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.*;

public class RequestsFragment extends Fragment {

    private DatabaseReference accountsRef;
    private ValueEventListener requestListener;
    private LinearLayout layout;

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
        requestListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot){
                        layout.removeAllViews();
                    if (!snapshot.exists()) {
                        TextView tv = new TextView(getContext());
                        tv.setText("No requests yet.");
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
                                                    });
                                        })
                                        .setNegativeButton("Reject", (dialog, which) -> {
                                            ds.getRef().child("Status").setValue(1)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(getContext(), "Rejected " + first + " " + last, Toast.LENGTH_SHORT).show();
                                                    });
                                        })
                                        .setNeutralButton("Cancel", null)
                                        .show();
                            });

                            layout.addView(btn);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Failed to load requests.", Toast.LENGTH_SHORT).show();
                    }
                };
        accountsRef.orderByChild("Status").equalTo(0).addValueEventListener(requestListener);
        return scrollView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (requestListener != null) {
            accountsRef.removeEventListener(requestListener);
        }
    }
}
