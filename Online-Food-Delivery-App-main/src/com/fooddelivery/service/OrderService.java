package com.fooddelivery.service;

import com.fooddelivery.model.FoodItem;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer that encapsulates backend order operations.
 */
public class OrderService {

    /**
     * Adds a validated item to the order and recalculates base amount.
     */
    public void addItemToOrder(Order order, FoodItem item) {
        if (item.getPrice() <= 0) {
            throw new IllegalArgumentException("Item price must be greater than 0.");
        }
        order.getItems().add(item);
        calculateBaseAmountFromItems(order);
    }

    public boolean removeItemFromOrder(Order order, int itemId) {
        boolean removed = order.getItems().removeIf(item -> item.getItemId() == itemId);
        if (removed) {
            calculateBaseAmountFromItems(order);
        }
        return removed;
    }

    public void calculateBaseAmountFromItems(Order order) {
        double baseAmount = 0;
        for (FoodItem item : order.getItems()) {
            baseAmount += item.getPrice();
        }
        order.setBaseAmount(baseAmount);
    }

    /**
     * Applies supported coupon codes to an order.
     */
    public boolean applyCoupon(Order order, String couponCode) {
        String normalizedCode = couponCode == null ? "" : couponCode.trim().toUpperCase();
        double discountAmount = 0;

        switch (normalizedCode) {
            case "FLAT50":
                discountAmount = 50;
                break;
            case "SAVE10":
                discountAmount = order.getBaseAmount() * 0.10;
                break;
            default:
                return false;
        }

        order.setCouponCode(normalizedCode);
        order.setCouponDiscount(discountAmount);
        return true;
    }

    public void updateOrderStatus(Order order, OrderStatus status) {
        order.setStatus(status);
    }

    public void printOrderSummary(Order order) {
        double finalBill = order.calculateBill();

        System.out.println("\n========== ORDER SUMMARY ==========");
        System.out.println("Order ID      : " + order.getOrderId());
        System.out.println("Type          : " + order.getClass().getSimpleName());
        System.out.println("Status        : " + order.getStatus());
        System.out.println("Order Time    : " + order.getOrderTime());
        System.out.println("Items         :");

        if (order.getItems().isEmpty()) {
            System.out.println("  No items added yet.");
        } else {
            for (FoodItem item : order.getItems()) {
                System.out.println("  - " + item);
            }
        }

        System.out.printf("Base Amount   : %.2f%n", order.getBaseAmount());
        System.out.printf("Coupon (%s) : -%.2f%n", order.getCouponCode(), order.getCouponDiscount());
        System.out.printf("Tax (5%%)      : %.2f%n", order.getTaxAmount());
        System.out.printf("Delivery      : %.2f%n", Order.DELIVERY_CHARGE);
        System.out.printf("Final Bill    : %.2f%n", finalBill);
        System.out.println("===================================\n");
    }

    /**
     * Demonstrates polymorphism by calling calculateBill() on Order references.
     */
    public void processAllOrders(List<Order> orders) {
        double revenue = 0;
        System.out.println("\n===== PROCESSING ALL ORDERS (POLYMORPHISM) =====");

        for (Order order : orders) {
            double bill = order.calculateBill();
            revenue += bill;
            System.out.printf("Order %d [%s] -> Final Bill: %.2f%n",
                    order.getOrderId(), order.getClass().getSimpleName(), bill);
        }

        System.out.printf("Total Revenue: %.2f%n", revenue);
        System.out.println("===============================================\n");
    }

    public double calculateTotalRevenue(List<Order> orders) {
        double revenue = 0;
        for (Order order : orders) {
            revenue += order.calculateBill();
        }
        return revenue;
    }

    public List<Order> getOrdersByType(List<Order> orders, Class<? extends Order> orderType) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (orderType.isInstance(order)) {
                result.add(order);
            }
        }
        return result;
    }
}
