package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.project.fragments.ChatFragment;
import com.example.project.fragments.DepositFragment;
import com.example.project.fragments.OverviewFragment;
import com.example.project.fragments.SettingsFragment;
import com.example.project.fragments.TradeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    public String currentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.actionGroupOverview:
                        fragment = new OverviewFragment();
                        break;
                    case R.id.actionDeposit:
                        fragment = new DepositFragment();
                        break;
                    case R.id.actionChat:
                        fragment = new ChatFragment();
                        break;
                    case R.id.actionTrade:
                        fragment = new TradeFragment();
                        break;
                    case R.id.actionSettings:
                        fragment = new SettingsFragment();
                        break;
                    default:
                        fragment = new OverviewFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.actionGroupOverview);
    }


}