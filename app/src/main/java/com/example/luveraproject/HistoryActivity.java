package com.example.luveraproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_history);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                // Cukup tutup aktivitas ini, kembali ke Home yang ada di stack
                finish();
                overridePendingTransition(0, 0);
                return true;
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        // Cukup tutup aktivitas ini, kembali ke Home
        finish();
        overridePendingTransition(0, 0);
    }
}