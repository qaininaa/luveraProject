package com.example.luveraproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luveraproject.Model.CartItem;
import com.example.luveraproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartVH> {

    public interface OnCartChanged {
        void onCartUpdated();
    }

    private final Context ctx;
    private final List<CartItem> list;
    private final OnCartChanged listener;
    private final DatabaseReference cartRef;

    public CartAdapter(Context ctx, List<CartItem> list, String userId, OnCartChanged l) {
        this.ctx = ctx;
        this.list = list;
        this.listener = l;
        this.cartRef = FirebaseDatabase.getInstance()
                .getReference("Carts").child(userId);
    }

    @NonNull @Override
    public CartVH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View vItem = LayoutInflater.from(ctx).inflate(R.layout.itemcart, p, false);
        return new CartVH(vItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CartVH h, int pos) {
        CartItem ci = list.get(pos);

        // Hapus listener lama dulu
        h.checkBox.setOnCheckedChangeListener(null);

        // Set status checkbox sesuai data
        h.checkBox.setChecked(ci.checked);

        // Pasang listener baru
        h.checkBox.setOnCheckedChangeListener((b, isChk) -> {
            ci.checked = isChk;
            save(ci);
            listener.onCartUpdated();
        });


        h.textName.setText(ci.name);
        h.textPrice.setText("Rp" + String.format("%,.0f", ci.price));
        h.textQty.setText("x" + ci.quantity);

        if (ci.image != null && ci.image.startsWith("http"))
            Glide.with(ctx).load(ci.image).placeholder(R.drawable.kategori_default)
                    .into(h.imageProduct);
        else {
            int id = ctx.getResources().getIdentifier(ci.image,"drawable",ctx.getPackageName());
            h.imageProduct.setImageResource(id!=0 ? id : R.drawable.kategori_default);
        }

        h.btnPlus.setOnClickListener(v -> {
            ci.quantity++;
            notifyItemChanged(pos);
            save(ci);
            listener.onCartUpdated();
        });

        h.btnDelete.setOnClickListener(v -> {
            if (ci.quantity>1){
                ci.quantity--;
                notifyItemChanged(pos);
                save(ci);
                listener.onCartUpdated();
            }
        });

    }

    @Override public int getItemCount() { return list.size(); }
    private void save(CartItem ci){
        if(cartRef!=null && ci.key!=null){
            cartRef.child(ci.key).setValue(ci);
        }
    }

    static class CartVH extends RecyclerView.ViewHolder {
        CheckBox   checkBox;
        ImageView  imageProduct;
        TextView   textName, textPrice, textQty;
        ImageButton btnPlus, btnDelete;
        CartVH(@NonNull View v){
            super(v);
            checkBox      = v.findViewById(R.id.checkboxSelect);
            imageProduct  = v.findViewById(R.id.imageProduct);
            textName      = v.findViewById(R.id.textName);
            textPrice     = v.findViewById(R.id.textPrice);
            textQty       = v.findViewById(R.id.textQty);
            btnPlus       = v.findViewById(R.id.addButton);
            btnDelete     = v.findViewById(R.id.deleteButton);
        }


    }
}