package com.example.luveraproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);

        ImageButton nextButton = findViewById(R.id.button_onboarding1);
        nextButton.setOnClickListener(v -> {
            startActivity(new Intent(OnboardingActivity.this, Onboarding2Activity.class));
            finish();
        });
    }
}