package com.example.luveraproject;

public class Product {
    private int id;
    private String name;
    private double price;
    private String category;
    private String image;
    private int isNew;
    private String description;

    public Product(int id, String name, double price, String category, String image, int isNew, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.image = image;
        this.isNew = isNew;
        this.description = description;
    }

    // Getter methods
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
    public String getCategory() {
        return category;
    }
}
