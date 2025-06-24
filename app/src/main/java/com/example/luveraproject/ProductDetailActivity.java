package com.example.luveraproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imageProduct;
    TextView textName, textPrice, textDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // tampilkan tombol back
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Detail Produk");
        }

        toolbar.setNavigationIcon(R.drawable.back_icon); // pastikan file ada di res/drawable

        // Aksi klik ikon back
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // View setup
        imageProduct = findViewById(R.id.imageProductDetail);
        textName = findViewById(R.id.textProductNameDetail);
        textPrice = findViewById(R.id.textProductPriceDetail);
        textDescription = findViewById(R.id.textProductDescription);

        // Get data dari intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 0);
        String image = intent.getStringExtra("image");
        String description = intent.getStringExtra("description");

        // Tampilkan data ke tampilan
        textName.setText(name);
        textPrice.setText("Rp" + String.format("%,.0f", price));
        textDescription.setText(description);

        // Tampilkan gambar
        if (image != null && image.startsWith("http")) {
            Glide.with(this)
                    .load(image)
                    .placeholder(R.drawable.kategori_default)
                    .into(imageProduct);
        } else {
            int imageResId = getResources().getIdentifier(image, "drawable", getPackageName());
            if (imageResId != 0) {
                imageProduct.setImageResource(imageResId);
            } else {
                imageProduct.setImageResource(R.drawable.kategori_default); // fallback
            }
        }

    }
}
