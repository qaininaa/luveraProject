package com.example.luveraproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout menuContainer;
    private TextView tvName, tvEmail, tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT); // Bikin status bar transparan
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            ); // Biar konten tetap "di bawah" status bar dan icon status bar gelap
        }


        menuContainer = findViewById(R.id.menuContainer);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);

        // ✅ Ambil data dari SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = sharedPref.getString("username", "Nama Pengguna");
        String email = sharedPref.getString("email", "email@example.com");
        String phone = sharedPref.getString("phone", "08xxxxxxxx");

        tvName.setText(username);
        tvEmail.setText(email);
        tvPhone.setText(phone);

        // Tambah menu
        addMenuItem(R.drawable.ic_home_profile, "My Account");
        addMenuItem(R.drawable.ic_address_profile, "Address");
        addMenuItem(R.drawable.ic_transportation_profile, "Transportation");
        addMenuItem(R.drawable.ic_logout_profile, "Logout");
        addMenuItem(R.drawable.ic_help_profile, "Help & Support");
        addMenuItem(R.drawable.ic_about_profile, "About App");

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;

            if (itemId == R.id.navigation_home) {
                intent = new Intent(this, HomeActivity.class);
            } else if (itemId == R.id.navigation_history) {
                intent = new Intent(this, HistoryActivity.class);
            } else if (itemId == R.id.navigation_cart) {
                intent = new Intent(this, CartActivity.class);
            }

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
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

        item.setOnClickListener(v -> {
            if (title.equalsIgnoreCase("Logout")) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences.Editor editor = getSharedPreferences("UserSession", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(ProfileActivity.this, GetstartedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
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
