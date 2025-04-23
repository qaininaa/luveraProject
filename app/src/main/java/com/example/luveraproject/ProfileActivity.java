package com.example.luveraproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout menuContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        menuContainer = findViewById(R.id.menuContainer);

        addMenuItem(R.drawable.ic_home_profile, "My Account");
        addMenuItem(R.drawable.ic_address_profile, "Address");
        addMenuItem(R.drawable.ic_transportation_profile, "Transportation");
        addMenuItem(R.drawable.ic_logout_profile, "Logout");
        addMenuItem(R.drawable.ic_help_profile, "Help & Support");
        addMenuItem(R.drawable.ic_about_profile, "About App");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.navigation_history) {
                Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return true;
        });
    }

    private void addMenuItem(int iconResId, String title) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View item = inflater.inflate(R.layout.item_profile_menu, menuContainer, false);

        ImageView icon = item.findViewById(R.id.menuIcon);
        TextView text = item.findViewById(R.id.menuTitle);

        icon.setImageResource(iconResId);
        text.setText(title);

        // Optional: OnClickListener
        item.setOnClickListener(v -> {
            // TODO: Add action based on title or a tag
        });

        menuContainer.addView(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

}
