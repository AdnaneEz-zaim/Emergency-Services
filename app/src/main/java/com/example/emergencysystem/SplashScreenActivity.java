package com.example.emergencysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.emergencysystem.Managers.CacheHelper;
import com.example.emergencysystem.Models.Role;
import com.example.emergencysystem.Models.User;

public class SplashScreenActivity extends AppCompatActivity {
    CacheHelper sqlLiteCacheHelper;
    User cacheUser;
    Intent switchToArView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sqlLiteCacheHelper = new CacheHelper(this);
        cacheUser = sqlLiteCacheHelper.getUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cacheUser != null) {
                    if (cacheUser.getRole().equals(Role.user))
                        switchToArView = new Intent(SplashScreenActivity.this, UserListUrgencesActivity.class);
                    else
                        switchToArView = new Intent(SplashScreenActivity.this, ListUrgencesPage.class);

                } else {
                    switchToArView = new Intent(SplashScreenActivity.this, MainActivity.class);
                }

                startActivity(switchToArView);
                finish();
            }
        }, 1500);
    }
}