package com.example.emergencysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button goToHome;
    TextView newAcompteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToHome = findViewById(R.id.goToHome);
        newAcompteBtn = findViewById(R.id.newAcompteBtn);

        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToArView = new Intent(MainActivity.this,ListUrgencesPage.class);
                startActivity(switchToArView);
            }
        });

        newAcompteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToArView = new Intent(MainActivity.this,CreateNewAcompte.class);
                startActivity(switchToArView);
            }
        });

    }
}