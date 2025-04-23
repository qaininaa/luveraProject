package com.example.luveraproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashscreenActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("onboardPrefs", MODE_PRIVATE);
            boolean isFirstTime = prefs.getBoolean("isFirstTime", true);

            if (isFirstTime) {
                startActivity(new Intent(SplashscreenActivity.this, OnboardingActivity.class));
            } else {
                startActivity(new Intent(SplashscreenActivity.this, MainActivity.class));
            }

            finish();
        }, SPLASH_DURATION);
    }
}