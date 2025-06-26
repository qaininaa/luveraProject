package com.example.luveraproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.luveraproject.Model.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productPrice, productDescription;
    private Button cartButton;
    private ImageButton backButton;

    private FirebaseAuth mAuth;
    private DatabaseReference cartRef;

    private String productKey; // ⬅️ Field global (bukan lokal)
    private String name;
    private double price;
    private String imageUrl;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Inisialisasi UI
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        cartButton = findViewById(R.id.cartButton);
        backButton = findViewById(R.id.backButton);

        // Firebase setup
        mAuth = FirebaseAuth.getInstance();
        cartRef = FirebaseDatabase.getInstance().getReference("Carts");

        // Ambil data dari intent dan simpan ke field global
        productKey = getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");
        price = getIntent().getDoubleExtra("price", 0.0);
        imageUrl = getIntent().getStringExtra("image");
        description = getIntent().getStringExtra("description");

        // Validasi productKey
        if (productKey == null || productKey.isEmpty()) {
            Toast.makeText(this, "Produk tidak valid (key kosong)", Toast.LENGTH_SHORT).show();
            finish(); // keluar dari activity
            return;
        }

        // Tampilkan informasi produk
        productName.setText(name);
        productPrice.setText("Rp" + String.format("%,.0f", price));
        productDescription.setText(description);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(productImage);
        } else {
            productImage.setImageResource(R.drawable.kategori_default);
        }

        // Tombol kembali
        backButton.setOnClickListener(v -> finish());

        // Tombol tambah ke keranjang
        cartButton.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() == null) {
                Toast.makeText(this, "Silakan login dulu", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = mAuth.getCurrentUser().getUid();

            DatabaseReference itemRef = cartRef.child(userId).child(productKey);

            itemRef.get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    Long qty = snapshot.child("quantity").getValue(Long.class);
                    int newQty = (qty == null ? 0 : qty.intValue()) + 1;

                    itemRef.child("quantity").setValue(newQty)
                            .addOnSuccessListener(a ->
                                    Toast.makeText(this, "Jumlah produk diperbarui (" + newQty + ")", Toast.LENGTH_SHORT).show());
                } else {
                    CartItem item = new CartItem(name, price, imageUrl, 1, true);
                    item.key = productKey;

                    itemRef.setValue(item)
                            .addOnSuccessListener(a ->
                                    Toast.makeText(this, "Ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show());
                }
            }).addOnFailureListener(e ->
                    Toast.makeText(this, "Gagal mengakses keranjang: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
