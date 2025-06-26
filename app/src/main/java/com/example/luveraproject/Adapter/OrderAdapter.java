package com.example.luveraproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luveraproject.Model.CartItem;
import com.example.luveraproject.Model.Order;
import com.example.luveraproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.*;

// ... imports tetap

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.VH> {
    private final Context ctx;
    private final List<Order> orders;
    private final String uid = FirebaseAuth.getInstance().getUid();
    private final Set<String> expanded = new HashSet<>();

    public OrderAdapter(Context ctx, List<Order> orders) {
        this.ctx = ctx;
        this.orders = orders;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTanggal, tvTotal, tvQty, tvName, tvLihatSemua;
        ImageView imgThumb;
        Button btnSelesai;
        LinearLayout layoutDetailProduk;

        VH(@NonNull View v) {
            super(v);
            tvTanggal     = v.findViewById(R.id.textTanggal);
            tvTotal       = v.findViewById(R.id.textTotal);
            tvQty         = v.findViewById(R.id.textQuantity);
            tvName        = v.findViewById(R.id.textProductName);
            tvLihatSemua  = v.findViewById(R.id.textLihatSemua);
            imgThumb      = v.findViewById(R.id.imageProduct);
            layoutDetailProduk = v.findViewById(R.id.layoutDetailProduk);
            btnSelesai    = v.findViewById(R.id.btnSelesai);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int vType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_order, p, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Order o = orders.get(pos);
        String key = o.key;

        h.tvTanggal.setText(format(o.createdAt));
        h.tvTotal.setText("Total: Rp" + String.format("%,.0f", o.total));

        int totalPcs = 0;
        CartItem mainProduct = null;
        int maxQty = -1;
        List<CartItem> detailItems = new ArrayList<>();

        // --- Cari produk qty terbesar
        if (o.items != null) {
            for (Map.Entry<String, CartItem> e : o.items.entrySet()) {
                CartItem c = e.getValue();
                if (c == null) continue;
                totalPcs += c.quantity;

                if (c.quantity > maxQty) {
                    if (mainProduct != null) detailItems.add(mainProduct); // pindah yang sebelumnya
                    mainProduct = c;
                    maxQty = c.quantity;
                } else {
                    detailItems.add(c);
                }
            }
        }

        if (mainProduct != null) {
            h.tvQty.setText(mainProduct.quantity + " pcs");   // hanya qty produk utama
        } else {
            h.tvQty.setText("");    // fallback kalau data hilang
        }

        // --- Tampilkan produk utama
        if (mainProduct != null) {
            h.tvName.setText(mainProduct.name);
            Glide.with(ctx)
                    .load(mainProduct.image)
                    .placeholder(R.drawable.kategori_default)
                    .into(h.imgThumb);
        } else {
            h.tvName.setText("Produk tidak tersedia");
            h.imgThumb.setImageResource(R.drawable.kategori_default);
        }

        // --- Tampilkan produk lainnya (lihat semua)
        h.layoutDetailProduk.removeAllViews();
        for (CartItem item : detailItems) {
            View row = LayoutInflater.from(ctx).inflate(R.layout.item_product_detail_row, h.layoutDetailProduk, false);
            TextView tvName = row.findViewById(R.id.tvName);
            TextView tvQty = row.findViewById(R.id.tvQty);
            ImageView img = row.findViewById(R.id.imgItem);

            tvName.setText(item.name);
            tvQty.setText("x" + item.quantity);
            Glide.with(ctx).load(item.image).placeholder(R.drawable.kategori_default).into(img);

            h.layoutDetailProduk.addView(row);
        }

        // --- Toggle "Lihat Semua"
        if (!detailItems.isEmpty()) {
            h.tvLihatSemua.setVisibility(View.VISIBLE);
            boolean isExpanded = expanded.contains(key);
            h.layoutDetailProduk.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            h.tvLihatSemua.setText(isExpanded ? "Sembunyikan" : "Lihat semua");

            h.tvLihatSemua.setOnClickListener(v -> {
                if (isExpanded) expanded.remove(key);
                else expanded.add(key);
                notifyItemChanged(pos);
            });
        } else {
            h.tvLihatSemua.setVisibility(View.GONE);
            h.layoutDetailProduk.setVisibility(View.GONE);
        }

        // --- Tombol selesai
        h.btnSelesai.setOnClickListener(v -> {
            if (!"done".equals(o.status)) {
                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("Orders")
                        .child(uid)
                        .child(o.key)
                        .child("status");

                ref.setValue("done").addOnSuccessListener(a -> {
                    Toast.makeText(ctx, "Pesanan ditandai selesai", Toast.LENGTH_SHORT).show();
                    o.status = "done";
                    notifyItemChanged(pos);
                });
            }
        });
    }



    @Override public int getItemCount() { return orders.size(); }

    private String format(long ms) {
        return new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                .format(new Date(ms));
    }
}

