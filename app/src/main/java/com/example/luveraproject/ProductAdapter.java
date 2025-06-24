package com.example.luveraproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.productList = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textName.setText(product.getName());
        holder.textPrice.setText("Rp" + String.format("%,.0f", product.getPrice()));

        String image = product.getImage();
        if (image.startsWith("http")) {
            // Load dari URL pakai Glide
            Glide.with(context)
                    .load(image)
                    .placeholder(R.drawable.kategori_default)
                    .into(holder.imageProduct);
        } else {
            // Load dari drawable lokal
            int imageResId = context.getResources().getIdentifier(
                    image, "drawable", context.getPackageName());

            if (imageResId != 0) {
                holder.imageProduct.setImageResource(imageResId);
            } else {
                holder.imageProduct.setImageResource(R.drawable.kategori_default); // fallback
            }
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textName, textPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textName = itemView.findViewById(R.id.textProductName);
            textPrice = itemView.findViewById(R.id.textProductPrice);
        }
    }
    public void updateData(List<Product> newProducts) {
        this.productList = newProducts;
        notifyDataSetChanged();
    }

}

