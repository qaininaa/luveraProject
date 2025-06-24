package com.example.luveraproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Onboarding3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding3);

        ImageButton nextButton = findViewById(R.id.button_onboarding3);
        nextButton.setOnClickListener(v -> {
            // Simpan bahwa user sudah selesai onboarding
            SharedPreferences prefs = getSharedPreferences("onboardPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();

            // Lanjut ke Get Started
            startActivity(new Intent(Onboarding3Activity.this, GetstartedActivity.class));
            finish();
        });
    }
}
