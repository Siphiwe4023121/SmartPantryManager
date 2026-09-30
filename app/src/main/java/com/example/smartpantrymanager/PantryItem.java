package com.example.smartpantrymanager;

public class PantryItem {

    private int id;
    private String name;
    private int quantity;
    private String category;
    private String expiryDate;

    public PantryItem(int id, String name, int quantity,
                      String category, String expiryDate) {

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}