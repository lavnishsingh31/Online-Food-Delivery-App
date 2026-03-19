package com.fooddelivery.model;

import java.time.LocalDateTime;
import java.util.List;

public class RegularOrder extends Order {

    public RegularOrder(int orderId, double baseAmount, LocalDateTime orderTime, List<FoodItem> items) {
        super(orderId, baseAmount, orderTime, items);
    }

    @Override
    protected double getDiscountedAmount() {
        return getBaseAmount();
    }

    @Override
    public double calculateBill() {
        double taxableAmount = getTaxableAmount();
        return taxableAmount + getTaxAmount() + DELIVERY_CHARGE;
    }
}
