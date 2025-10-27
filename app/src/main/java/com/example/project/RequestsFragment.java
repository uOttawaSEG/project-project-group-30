package com.example.project;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestsFragment extends Fragment {
    private DatabaseReference requestsRef;

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

        requestsRef = FirebaseDatabase.getInstance().getReference("requests");



        if (MainActivity.requests.isEmpty()) {
            TextView tv = new TextView(getContext());
            tv.setText("No student requests yet.");
            layout.addView(tv);
            return scrollView;
        }

        for (String email : MainActivity.requests) {
            // find the full student info by email
            MainActivity.student s = null;
            for (MainActivity.student st : MainActivity.students) {
                if (st.getEmail().equals(email)) {
                    s = st;
                    break;
                }
            }
            if (s == null) continue;

            Button btn = new Button(getContext());
            btn.setText(s.getFullName());
            btn.setAllCaps(false);

            MainActivity.student finalS = s;
            btn.setOnClickListener(v -> {
                new AlertDialog.Builder(getContext())
                        .setTitle(finalS.getFullName())
                        .setMessage(
                                "Email: " + finalS.getEmail() + "\n" +
                                        "Phone: " + finalS.getPhone() + "\n" +
                                        "Program: " + finalS.getProgram()
                        )
                        .setPositiveButton("Approve", (dialog, which) -> {
                            MainActivity.studentAccounts.add(finalS.getEmail());
                            MainActivity.requests.remove(finalS.getEmail());
                            Toast.makeText(getContext(), "Approved " + finalS.getFullName(), Toast.LENGTH_SHORT).show();
                            refreshFragment();
                        })
                        .setNegativeButton("Reject", (dialog, which) -> {
                            MainActivity.rejected.add(finalS.getEmail());
                            MainActivity.requests.remove(finalS.getEmail());
                            Toast.makeText(getContext(), "Rejected " + finalS.getFullName(), Toast.LENGTH_SHORT).show();
                            refreshFragment();
                        })
                        .setNeutralButton("Cancel", null)
                        .show();
            });

            layout.addView(btn);
        }

        return scrollView;
    }

    private void refreshFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }
}
