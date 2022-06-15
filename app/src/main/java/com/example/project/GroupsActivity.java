package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class GroupsActivity extends AppCompatActivity {

    LinearLayout layGroup1;
    LinearLayout layGroup2;
    LinearLayout layGroup3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        this.getSupportActionBar().hide();

        LinearLayout layGroup1 = findViewById(R.id.layGroup1);
        LinearLayout layGroup2 = findViewById(R.id.layGroup2);
        LinearLayout layGroup3 = findViewById(R.id.layGroup3);

        layGroup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGroup();
            }
        });
        layGroup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGroup();
            }
        });
        layGroup3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGroup();
            }
        });
    }

    private void goToGroup() {
        // go to group
    }
}