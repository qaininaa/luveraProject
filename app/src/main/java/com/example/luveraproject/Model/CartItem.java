package com.example.luveraproject.Model;

import java.io.Serializable;

public class CartItem implements Serializable {
    public String name;
    public double price;
    public String image;
    public int quantity;
    public boolean checked;
    public String key;

    // Constructor kosong (wajib untuk Firebase)
    public CartItem() {
    }

    // Constructor lengkap
    public CartItem(String name, double price, String image, int quantity, boolean checked) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.checked = checked;
    }

    // Getter dan Setter (opsional, tapi disarankan)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
