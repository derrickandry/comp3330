package com.example.comp3330assistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private MaterialButton submitButton, viewButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submitButton = findViewById(R.id.submitProject);
        viewButton = findViewById(R.id.viewProjects);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, ProjectCreation.class);
                startActivity(intent);
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, GroupList.class);
                startActivity(intent);
            }
        });


    }
}