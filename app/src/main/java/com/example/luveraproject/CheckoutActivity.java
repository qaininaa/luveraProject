// CheckoutActivity.java
package com.example.luveraproject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luveraproject.Adapter.CheckoutProductAdapter;
import com.example.luveraproject.Model.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvSubtotal, tvOngkir, tvBiayaLayanan, tvTotal;


    private TextView tvRumahUsername;

    private double subtotal;
    private double ongkir, biayalayanan;
    private double total;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_checkout);

        tvSubtotal      = findViewById(R.id.tvSubtotal);
        tvOngkir        = findViewById(R.id.tvOngkir);
        tvBiayaLayanan  = findViewById(R.id.tvBiayaLayanan);
        tvTotal         = findViewById(R.id.tvTotal);
        tvRumahUsername = findViewById(R.id.tvRumahUsername);

        // Ambil data nama user dari Firebase
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        userRef.child("username").get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String username = snapshot.getValue(String.class);
                tvRumahUsername.setText("Rumah. " + username);
            }
        });
        TextView tvAddress = findViewById(R.id.tvAddress);
        userRef.child("address").get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                tvAddress.setText(snapshot.getValue(String.class));
            }
        });

        findViewById(R.id.buttonPay).setOnClickListener(v -> createOrder());

        RecyclerView recycler = findViewById(R.id.recyclerCheckoutItems);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<CartItem> selectedItems =
                (ArrayList<CartItem>) getIntent().getSerializableExtra("selectedItems");

        if (selectedItems != null && !selectedItems.isEmpty()) {
            CheckoutProductAdapter adapter = new CheckoutProductAdapter(this, selectedItems);
            recycler.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Tidak ada produk yang diterima", Toast.LENGTH_SHORT).show();
        }

        subtotal = 0;  // BENAR
        for (CartItem item : selectedItems) {
            subtotal += item.price * item.quantity;
        }

        ongkir = 10000;
        biayalayanan = 2500;
        total = subtotal + ongkir + biayalayanan;

        tvSubtotal.setText("Subtotal: Rp" + String.format("%,.0f", subtotal));
        tvOngkir.setText("Ongkir: Rp" + String.format("%,.0f", ongkir));
        tvBiayaLayanan.setText("Biaya Layanan: Rp" + String.format("%,.0f", biayalayanan));
        tvTotal.setText("TOTAL: Rp" + String.format("%,.0f", total));
    }

    private void createOrder() {
        if (FirebaseAuth.getInstance().getCurrentUser()==null) return;
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        // 1) ambil snapshot keranjang  items
        db.child("Carts").child(uid).get().addOnSuccessListener(cartSnap -> {

            if (!cartSnap.exists()) { Toast.makeText(this,"Keranjang kosong",Toast.LENGTH_SHORT).show(); return; }

            // 2) siapkan map order
            Map<String,Object> orderMap = new HashMap<>();
            orderMap.put("subtotal" , subtotal);
            orderMap.put("ongkir"   , ongkir);
            orderMap.put("fee"      , biayalayanan);
            orderMap.put("total"    , total);
            orderMap.put("status"   , "pending");
            orderMap.put("createdAt", ServerValue.TIMESTAMP);


            // 3) push order
            DatabaseReference orderRef = db.child("Orders").child(uid).push();
            orderRef.setValue(orderMap)
                    .addOnSuccessListener(a -> {
                        orderRef.child("items").setValue(cartSnap.getValue())
                                .addOnSuccessListener(x -> {
                                    db.child("Carts").child(uid).removeValue();
                                    Toast.makeText(this,"Order berhasil dibuat!",Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                    });

        }).addOnFailureListener(e ->
                Toast.makeText(this,"Gagal: "+e.getMessage(),Toast.LENGTH_SHORT).show());
    }
}
