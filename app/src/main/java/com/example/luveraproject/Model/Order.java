package com.example.luveraproject.Model;

import java.util.HashMap;

public class Order {
    public String key;                          // push-key order
    public double subtotal, ongkir, fee, total;
    public String status;                       // pending / selesai
    public long createdAt;                      // millis
    public HashMap<String, CartItem> items;     // <cartKey, CartItem>

    public Order() {}                           // wajib utk Firebase
}
