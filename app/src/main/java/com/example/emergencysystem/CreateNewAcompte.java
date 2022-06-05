package com.example.emergencysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CreateNewAcompte extends AppCompatActivity {
    TextView dejaCreeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_acompte);

        dejaCreeBtn = findViewById(R.id.dejaCreeBtn);
        dejaCreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchToArView = new Intent(CreateNewAcompte.this, MainActivity.class);
                startActivity(switchToArView);
            }
        });

    }
}