package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class welcome_tutor extends AppCompatActivity {
    // button for logging out the tutor and returning to the main screen
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the UI layout for this screen to activity_welcome_tutor.xml
        setContentView(R.layout.activity_welcome_tutor);
        // Link the button variable to the actual button in the layout
        btnLogout = findViewById(R.id.btnLogout);
        CalendarView calendar = findViewById(R.id.calendar);
        TextView dateView = findViewById(R.id.date_view);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                dateView.setText(date);
            }
        });
// when the button is clicked it does thew following instructions
        btnLogout.setOnClickListener(v -> {
            // create an Intent to navigate from the tutor welcome screen back to MainActivity
            Intent intent = new Intent(welcome_tutor.this, MainActivity.class);
            startActivity(intent);
        });
    }
}