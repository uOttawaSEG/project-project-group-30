package com.example.project;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RejectedFragment extends Fragment {

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

        if (MainActivity.rejected.isEmpty()) {
            TextView tv = new TextView(getContext());
            tv.setText("No rejected students yet.");
            layout.addView(tv);
            return scrollView;
        }

        for (String email : MainActivity.rejected) {
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
