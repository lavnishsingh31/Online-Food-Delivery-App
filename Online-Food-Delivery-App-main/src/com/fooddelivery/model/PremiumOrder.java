package com.fooddelivery.model;

import java.time.LocalDateTime;
import java.util.List;

public class PremiumOrder extends Order {

    public PremiumOrder(int orderId, double baseAmount, LocalDateTime orderTime, List<FoodItem> items) {
        super(orderId, baseAmount, orderTime, items);
    }

    @Override
    protected double getDiscountedAmount() {
        return getBaseAmount() * 0.80;
    }

    @Override
    public double calculateBill() {
        double taxableAmount = getTaxableAmount();
        return taxableAmount + getTaxAmount() + DELIVERY_CHARGE;
    }
}
