package com.example.project;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ApprovedFragment extends Fragment {

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

        if (MainActivity.studentAccounts.isEmpty()) {
            TextView tv = new TextView(getContext());
            tv.setText("No approved students yet.");
            layout.addView(tv);
            return scrollView;
        }

        for (String email : MainActivity.studentAccounts) {
            for (MainActivity.student s : MainActivity.students) {
                if (s.getEmail().equals(email)) {
                    TextView tv = new TextView(getContext());
                    tv.setText("• " + s.getFullName());
                    tv.setTextSize(18);
                    layout.addView(tv);
                }
            }
        }

        return scrollView;
    }
}
