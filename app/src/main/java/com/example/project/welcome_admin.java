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
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);

        // find views AFTER setContentView
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnLogout = findViewById(R.id.btnLogout);

        // create and set adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Requests");
                    break;
                case 1:
                    tab.setText("Rejections");
                    break;
                case 2:
                    tab.setText("Approved");
                    break;
            }
        }).attach();

        // logout button listener
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(welcome_admin.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
