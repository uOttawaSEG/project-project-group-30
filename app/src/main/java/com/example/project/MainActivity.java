package com.example.project;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declare Button object
    private Button nextActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize button using findViewById
        nextActivityButton = findViewById(R.id.Login);

        // Set click listener for the button
        nextActivityButton.setOnClickListener(v -> {
            // Create Intent to start SecondActivity
            Intent intent = new Intent(MainActivity.this, longin_as.class);
            // Start the activity
            startActivity(intent);
        });
    }
}