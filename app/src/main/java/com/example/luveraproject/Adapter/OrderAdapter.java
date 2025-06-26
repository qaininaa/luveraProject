package com.example.luveraproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luveraproject.Model.Order;
import com.example.luveraproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderVH> {

    private final Context context;
    private final List<Order> list;

    public OrderAdapter(Context context, List<Order> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderVH holder, int position) {
        Order order = list.get(position);

        holder.textTanggal.setText("Tanggal: " + formatDate(order.createdAt));
        holder.textTotal.setText("Total: Rp" + String.format("%,.0f", order.total));
        int jumlahProduk = (order.items != null) ? order.items.size() : 0;
        holder.textItemCount.setText("Jumlah Produk: " + jumlahProduk);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class OrderVH extends RecyclerView.ViewHolder {
        TextView textTanggal, textTotal, textItemCount;

        public OrderVH(@NonNull View itemView) {
            super(itemView);
            textTanggal = itemView.findViewById(R.id.textTanggal);
            textTotal = itemView.findViewById(R.id.textTotal);
            textItemCount = itemView.findViewById(R.id.textItemCount);
        }
    }

    private String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(new Date(millis));
    }
}
