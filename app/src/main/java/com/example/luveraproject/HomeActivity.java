package com.example.luveraproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luveraproject.Adapter.CategoryAdapter;
import com.example.luveraproject.Adapter.ProductAdapter;
import com.example.luveraproject.Model.Category;
import com.example.luveraproject.Model.Product;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private List<Product> allProducts = new ArrayList<>();
    private TextView welcomeText;
    private ShimmerFrameLayout shimmerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        shimmerLayout = findViewById(R.id.shimmerLayout);
        shimmerLayout.startShimmer();

        productList = new ArrayList<>();

        welcomeText = findViewById(R.id.textWelcome);

        // Ambil username dari SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = sharedPref.getString("username", "Pengguna");
        welcomeText.setText("Hai, " + username);

        loadProductsFromFirebase();

        productAdapter = new ProductAdapter(this, productList, product -> {
            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("image", product.getImage());
            intent.putExtra("description", product.getDescription());
            startActivity(intent);
        });

        recyclerView.setAdapter(productAdapter);

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

    private void loadProductsFromFirebase() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Products");

        productList.clear();
        allProducts.clear();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Long idLong = data.child("id").getValue(Long.class);
                    int id = idLong != null ? idLong.intValue() : 0;
                    String name = data.child("name").getValue(String.class);
                    Double price = data.child("price").getValue(Double.class);
                    String category = data.child("category").getValue(String.class);
                    String image = data.child("image").getValue(String.class);
                    Boolean isNewValue = data.child("is_new").getValue(Boolean.class);
                    String description = data.child("description").getValue(String.class);
                    Long stockLong = data.child("stock").getValue(Long.class);
                    int stock = stockLong != null ? stockLong.intValue() : 0;
                    boolean isNew = isNewValue != null && isNewValue;

                    Product product = new Product(
                            id,
                            name,
                            price != null ? price : 0.0,
                            category,
                            image,
                            isNew,
                            description,
                            stock
                    );

                    productList.add(product);
                    allProducts.add(product);
                }

                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                productAdapter.updateData(productList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Gagal memuat produk: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
