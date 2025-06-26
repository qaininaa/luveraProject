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
    private String key; // ⬅️ Tambahan penting

    public Product() {}

    public Product(int id, String name, double price, String category, String image,
                   boolean isNew, String description, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.image = image;
        this.isNew = isNew;
        this.description = description;
        this.stock = stock;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public boolean isNew() { return isNew; }
    public void setNew(boolean aNew) { isNew = aNew; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
}
