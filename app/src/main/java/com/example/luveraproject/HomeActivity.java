package com.example.luveraproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> allProducts = new ArrayList<>(); // ← penting untuk filter kategori
    private DatabaseHelper dbHelper;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        dbHelper = new DatabaseHelper(this);
        productList = new ArrayList<>();

        welcomeText = findViewById(R.id.textWelcome);

        // Ambil username dari SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = sharedPref.getString("username", "Pengguna");
        welcomeText.setText("Hai, " + username);

        loadProductsFromDatabase();

        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        // Setup kategori horizontal
        RecyclerView recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Category> categories = Arrays.asList(
                new Category("Semua", R.drawable.kategori_default),
                new Category("Makeup", R.drawable.kategori_makeup),
                new Category("Skincare", R.drawable.kategori_skincare),
                new Category("Bodycare", R.drawable.kategori_bodycare),
                new Category("Haircare", R.drawable.kategori_haircare)
        );

        CategoryAdapter categoryAdapter = new CategoryAdapter(categories, categoryName -> {
            // Filter produk berdasarkan kategori
            if (categoryName.equalsIgnoreCase("Semua")) {
                productAdapter.updateData(allProducts);
            } else {
                List<Product> filtered = allProducts.stream()
                        .filter(p -> p.getCategory().equalsIgnoreCase(categoryName))
                        .collect(Collectors.toList());
                productAdapter.updateData(filtered);
            }
        });
        recyclerViewCategory.setAdapter(categoryAdapter);

        // Setup bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_history) {
                startActivity(new Intent(this, HistoryActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.navigation_cart) {
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return true;
        });
    }

    private void loadProductsFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);

        productList.clear();
        allProducts.clear();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                int isNew = cursor.getInt(cursor.getColumnIndexOrThrow("is_new"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                Product product = new Product(id, name, price, category, image, isNew, description);
                productList.add(product);
                allProducts.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            if (backToast != null) backToast.cancel();
            super.onBackPressed();
        } else {
            backToast = Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT);
            backToast.show();
            backPressedTime = System.currentTimeMillis();
        }
    }
}
