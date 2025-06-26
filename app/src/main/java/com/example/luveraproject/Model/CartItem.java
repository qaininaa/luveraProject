package com.example.luveraproject.Model;

public class CartItem {

    public String name;
    public double price;
    public String image;
    public int quantity;
    public boolean checked;
    public String key;

    public CartItem() { }

    public CartItem(String name, double price, String image, int quantity, boolean checked) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.checked = checked;
    }
}
