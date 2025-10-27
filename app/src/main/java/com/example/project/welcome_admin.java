package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class welcome_admin extends AppCompatActivity {

    private Button btnLogout;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);

        btnLogout = findViewById(R.id.btnLogout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // Set up ViewPager adapter
        viewPager.setAdapter(new ViewPagerAdapter(this));

        // Attach TabLayout and set tab titles
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("Requests"); break;
                        case 1: tab.setText("Rejected"); break;
                        case 2: tab.setText("Approved"); break;
                    }
                }).attach();

        // Logout button
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(welcome_admin.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
