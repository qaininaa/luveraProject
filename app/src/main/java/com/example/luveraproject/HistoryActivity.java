package com.example.luveraproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luveraproject.Adapter.OrderAdapter;
import com.example.luveraproject.Model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private final List<Order> orderList = new ArrayList<>();
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView rv = findViewById(R.id.recyclerHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(this, orderList);
        rv.setAdapter(adapter);

        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Orders")
                .child(uid);

        // ambil order, urutkan terbaru di atas
        ref.orderByChild("createdAt")
                .addValueEventListener(new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot snap) {
                        orderList.clear();
                        for (DataSnapshot s : snap.getChildren()) {
                            Order o = s.getValue(Order.class);
                            if (o != null) {
                                o.key = s.getKey();      // simpan push-key
                                orderList.add(0, o);     // terbaru di atas
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override public void onCancelled(@NonNull DatabaseError e) {
                        Toast.makeText(HistoryActivity.this,
                                "Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.navigation_history);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home)    startActivity(new Intent(this, HomeActivity.class));
            if (id == R.id.navigation_cart)    startActivity(new Intent(this, CartActivity.class));
            if (id == R.id.navigation_profile) startActivity(new Intent(this, ProfileActivity.class));
            overridePendingTransition(0,0);
            finish();
            return true;
        });
    }

    @Override public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
}
