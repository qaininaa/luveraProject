package com.example.luveraproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luveraproject.Adapter.CartAdapter;
import com.example.luveraproject.Model.CartItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity
        implements CartAdapter.OnCartChanged {

    private RecyclerView recyclerCart;
    private CartAdapter adapter;
    private final List<CartItem> cartList = new ArrayList<>();

    private TextView textTotal;
    private Button buttonCheckout;

    private FirebaseAuth      mAuth;
    private DatabaseReference cartRef;
    private String            uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        // ---- view binding
        recyclerCart   = findViewById(R.id.recyclerCart);
        textTotal      = findViewById(R.id.textTotal);
        buttonCheckout = findViewById(R.id.buttonCheckout);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        // ---- Firebase
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this,"Silakan login dahulu",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        uid     = mAuth.getUid();
        cartRef = FirebaseDatabase.getInstance().getReference("Carts").child(uid);

        // ---- adapter
        adapter = new CartAdapter(this, cartList, uid, this);
        recyclerCart.setAdapter(adapter);

        // ---- load data
        loadCart();

        // ---- checkout
        buttonCheckout.setOnClickListener(v -> {
            // Ambil produk yang dicentang (checked = true)
            ArrayList<CartItem> selectedItems = new ArrayList<>();
            for (CartItem item : cartList) {
                if (item.checked) selectedItems.add(item);
            }

            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Pilih minimal 1 produk untuk checkout", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kirim ke CheckoutActivity
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("selectedItems", selectedItems);
            startActivity(intent);
        });



        // ---- bottom nav
        setupBottomNav();
    }

    private void loadCart() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                cartList.clear();
                for (DataSnapshot s : snap.getChildren()) {
                    CartItem ci = s.getValue(CartItem.class);
                    if (ci != null) {
                        ci.key = s.getKey();          // untuk edit / delete nantinya
                        cartList.add(ci);
                    }
                }
                adapter.notifyDataSetChanged();
                updateSummary();
            }
            @Override public void onCancelled(DatabaseError err) {
                Toast.makeText(CartActivity.this,
                        "Gagal memuat keranjang: " + err.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override public void onCartUpdated() { updateSummary(); }

    /** Hitung total untuk item yang dicentang */
    private void updateSummary() {
        double total = 0;
        int    count = 0;

        for (CartItem c : cartList) {
            if (c.checked) {
                total += c.price * c.quantity;
                count += c.quantity;
            }
        }
        textTotal.setText("Total: Rp" + String.format("%,.0f", total));
        buttonCheckout.setText("Beli (" + count + ")");
        buttonCheckout.setEnabled(count > 0);
    }

    /* ---------- Bottom Navigation ---------- */
    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.navigation_cart);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home)        startActivity(new Intent(this, HomeActivity.class));
            else if (id == R.id.navigation_profile) startActivity(new Intent(this, ProfileActivity.class));
            else if (id == R.id.navigation_history) startActivity(new Intent(this, HistoryActivity.class));
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
