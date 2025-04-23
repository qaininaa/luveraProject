package com.example.luveraproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class GetstartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_getstarted);
        Button nextButton = findViewById(R.id.login);
        Button nextButton2 = findViewById(R.id.signup);
        nextButton.setOnClickListener(v -> {
            startActivity(new Intent(GetstartedActivity.this, LoginActivity.class));
            finish();
        });
        nextButton2.setOnClickListener(v -> {
            startActivity(new Intent(GetstartedActivity.this, SignupActivity.class));
            finish();
        });
    }
}