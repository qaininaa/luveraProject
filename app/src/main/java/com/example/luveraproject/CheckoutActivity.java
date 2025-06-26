// CheckoutActivity.java
package com.example.luveraproject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luveraproject.Model.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private TextView textTotal, textOngkir, textSubtotal;

    private double subtotal;
    private double shipping;
    private double total;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_checkout);   // bikin layout sangat simpel dulu
        textTotal    = findViewById(R.id.tvTotal);
        /* ---------- ambil data dari intent ---------- */
        textTotal   .setText("TOTAL     : Rp" + String.format("%,.0f", total));
        findViewById(R.id.buttonPay).setOnClickListener(v -> createOrder());
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
            orderMap.put("total"    , total);
            orderMap.put("status"   , "pending");
            orderMap.put("createdAt", com.google.firebase.database.ServerValue.TIMESTAMP);

            // 3) push order
            DatabaseReference orderRef = db.child("Orders").child(uid).push();
            orderRef.setValue(orderMap)                        // header
                    .addOnSuccessListener(a -> {
                        // 3b) salin items di /items
                        orderRef.child("items").setValue(cartSnap.getValue())
                                .addOnSuccessListener(x -> {
                                    // 4) bersihkan keranjang
                                    db.child("Carts").child(uid).removeValue();
                                    Toast.makeText(this,"Order berhasil dibuat!",Toast.LENGTH_SHORT).show();
                                    finish();      // balik
                                });
                    });

        }).addOnFailureListener(e ->
                Toast.makeText(this,"Gagal: "+e.getMessage(),Toast.LENGTH_SHORT).show());
    }
}
