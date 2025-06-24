package com.example.luveraproject.Model;

public class Product {
    private int id;
    private String name;
    private double price;
    private String category;
    private String image;
    private boolean isNew;
    private String description;
    private int stock;


    public Product(int id, String name, double price, String category, String image, boolean isNew, String description, int stock){
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.image = image;
        this.isNew = isNew;
        this.description = description;
        this.stock = stock;
    }

    // Getter methods
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
    public String getDescription() { return description; }

    public String getCategory() {
        return category;
    }
    public boolean getIsNew() {
        return isNew;
    }

    public int getStock() { return stock; }

}
