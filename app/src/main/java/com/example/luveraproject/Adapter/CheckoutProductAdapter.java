package com.example.luveraproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luveraproject.Model.CartItem;
import com.example.luveraproject.R;

import java.util.List;

public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.ProductVH> {

    private final Context context;
    private final List<CartItem> list;

    public CheckoutProductAdapter(Context context, List<CartItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout_product, parent, false);
        return new ProductVH(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, int position) {
        CartItem item = list.get(position);

        holder.textName.setText(item.name);
        holder.textPrice.setText("Rp" + String.format("%,.0f", item.price * item.quantity));
        holder.textQuantity.setText("x" + item.quantity);

        if (item.image != null && item.image.startsWith("http")) {
            Glide.with(context).load(item.image)
                    .placeholder(R.drawable.kategori_default)
                    .into(holder.imageProduct);
        } else {
            int id = context.getResources().getIdentifier(item.image, "drawable", context.getPackageName());
            holder.imageProduct.setImageResource(id != 0 ? id : R.drawable.kategori_default);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ProductVH extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textName, textPrice;
        TextView textQuantity;

        public ProductVH(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textName     = itemView.findViewById(R.id.textName);
            textPrice    = itemView.findViewById(R.id.textPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
        }

    }
}

