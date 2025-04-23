package com.example.luveraproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Onboarding2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding2);
        ImageButton nextButton = findViewById(R.id.button_onboarding2);
        nextButton.setOnClickListener(v -> {
            startActivity(new Intent(Onboarding2Activity.this, Onboarding3Activity.class));
            finish();
        });
    }
}