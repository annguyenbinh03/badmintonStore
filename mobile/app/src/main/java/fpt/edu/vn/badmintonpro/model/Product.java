package fpt.edu.vn.badmintonpro.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("racketId")
    private int id;

    private String name;
    private String description;
    private int price;
    private int brandId;
    private String brandName;
    private String weight;
    private String balance;
    private String tension;
    private boolean active;
    private String imageUrl;
    private int quantity;
    private String createdAt;

    public Product(int id, String name, String description, int price, int brandId, String brandName,
                   String weight, String balance, String tension, boolean active, String imageUrl,
                   int quantity, String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.brandId = brandId;
        this.brandName = brandName;
        this.weight = weight;
        this.balance = balance;
        this.tension = tension;
        this.active = active;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTension() {
        return tension;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
