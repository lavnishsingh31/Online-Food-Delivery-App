package com.fooddelivery.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Base abstraction for all order types.
 * Demonstrates inheritance, encapsulation, and runtime polymorphism.
 */
public abstract class Order {
    public static final double DELIVERY_CHARGE = 40.0;

    private final int orderId;
    private double baseAmount;
    private final LocalDateTime orderTime;
    private final List<FoodItem> items;
    private OrderStatus status;
    private String couponCode;
    private double couponDiscount;

    protected Order(int orderId, double baseAmount, LocalDateTime orderTime, List<FoodItem> items) {
        this.orderId = orderId;
        this.baseAmount = baseAmount;
        this.orderTime = orderTime;
        this.items = items == null ? new ArrayList<>() : items;
        this.status = OrderStatus.PLACED;
        this.couponCode = "NONE";
        this.couponDiscount = 0.0;
    }

    public abstract double calculateBill();

    public int getOrderId() {
        return orderId;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public List<FoodItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    /**
     * Calculates tax amount at fixed 5% on taxable amount.
     */
    public double getTaxAmount() {
        return getTaxableAmount() * 0.05;
    }

    public double getTaxableAmount() {
        return Math.max(0, getDiscountedAmount() - couponDiscount);
    }

    protected abstract double getDiscountedAmount();
}
