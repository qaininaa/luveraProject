package com.example.luveraproject.Model;

import java.util.Map;

public class Order {
    public double subtotal;
    public double ongkir;
    public double fee;
    public double total;
    public String status;
    public long createdAt;
    public Map<String, CartItem> items;

    public Order() {} // Wajib untuk Firebase
}
