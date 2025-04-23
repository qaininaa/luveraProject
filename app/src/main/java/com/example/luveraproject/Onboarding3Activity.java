package com.example.luveraproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Onboarding3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding3);
        ImageButton nextButton = findViewById(R.id.button_onboarding3);
        nextButton.setOnClickListener(v -> {
            startActivity(new Intent(Onboarding3Activity.this, GetstartedActivity.class));
            finish();
        });
    }
}