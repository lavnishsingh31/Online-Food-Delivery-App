package com.fooddelivery.model;

/**
 * Represents a single food item in an order.
 */
public class FoodItem {
    private final int itemId;
    private final String name;
    private final double price;
    private final FoodCategory category;

    public FoodItem(int itemId, String name, double price, FoodCategory category) {
        if (price <= 0) {
            throw new IllegalArgumentException("Item price must be greater than 0.");
        }
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public FoodCategory getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                ", price=" + String.format("%.2f", price) +
                ", category=" + category +
                '}';
    }
}
