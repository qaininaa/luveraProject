package com.example.luveraproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashscreenActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000;

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
                startActivity(new Intent(SplashscreenActivity.this, GetstartedActivity.class));
            }

            finish();
        }, SPLASH_DURATION);
    }
}
