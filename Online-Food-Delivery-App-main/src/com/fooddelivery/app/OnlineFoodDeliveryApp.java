package com.fooddelivery.app;

import com.fooddelivery.model.FoodCategory;
import com.fooddelivery.model.FoodItem;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderStatus;
import com.fooddelivery.model.PremiumOrder;
import com.fooddelivery.model.RegularOrder;
import com.fooddelivery.service.OrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OnlineFoodDeliveryApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderService orderService = new OrderService();
        List<Order> orders = new ArrayList<>();

        int orderCounter = 1001;
        int itemCounter = 1;

        while (true) {
            printMenu();
            System.out.print("Choose option: ");
            int choice = readInt(scanner);

            switch (choice) {
                case 1:
                    Order regularOrder = new RegularOrder(orderCounter++, 0, LocalDateTime.now(), new ArrayList<>());
                    orders.add(regularOrder);
                    System.out.println("Regular order created with ID: " + regularOrder.getOrderId());
                    break;

                case 2:
                    Order premiumOrder = new PremiumOrder(orderCounter++, 0, LocalDateTime.now(), new ArrayList<>());
                    orders.add(premiumOrder);
                    System.out.println("Premium order created with ID: " + premiumOrder.getOrderId());
                    break;

                case 3:
                    Order addTarget = selectOrder(scanner, orders);
                    if (addTarget == null) {
                        break;
                    }

                    scanner.nextLine();
                    System.out.print("Enter item name: ");
                    String itemName = scanner.nextLine();

                    System.out.print("Enter item price: ");
                    double price = readDouble(scanner);

                    FoodCategory category = readCategory(scanner);
                    try {
                        FoodItem item = new FoodItem(itemCounter++, itemName, price, category);
                        orderService.addItemToOrder(addTarget, item);
                        System.out.println("Item added successfully.");
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                    break;

                case 4:
                    Order removeTarget = selectOrder(scanner, orders);
                    if (removeTarget == null) {
                        break;
                    }

                    System.out.print("Enter itemId to remove: ");
                    int itemId = readInt(scanner);
                    boolean removed = orderService.removeItemFromOrder(removeTarget, itemId);
                    System.out.println(removed ? "Item removed successfully." : "Item not found.");
                    break;

                case 5:
                    Order couponTarget = selectOrder(scanner, orders);
                    if (couponTarget == null) {
                        break;
                    }

                    scanner.nextLine();
                    System.out.print("Enter coupon code (FLAT50 / SAVE10): ");
                    String couponCode = scanner.nextLine();
                    boolean applied = orderService.applyCoupon(couponTarget, couponCode);
                    System.out.println(applied ? "Coupon applied." : "Invalid coupon code.");
                    break;

                case 6:
                    if (orders.isEmpty()) {
                        System.out.println("No orders available.");
                        break;
                    }
                    viewOrdersByType(scanner, orderService, orders);
                    break;

                case 7:
                    if (orders.isEmpty()) {
                        System.out.println("No orders to process.");
                        break;
                    }
                    orderService.processAllOrders(orders);
                    break;

                case 8:
                    Order statusTarget = selectOrder(scanner, orders);
                    if (statusTarget == null) {
                        break;
                    }
                    OrderStatus status = readOrderStatus(scanner);
                    orderService.updateOrderStatus(statusTarget, status);
                    System.out.println("Order status updated.");
                    break;

                case 9:
                    System.out.printf("Exiting app. Total revenue so far: %.2f%n", orderService.calculateTotalRevenue(orders));
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please choose between 1 and 9.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n========== ONLINE FOOD DELIVERY APP ==========");
        System.out.println("1. Create Regular Order");
        System.out.println("2. Create Premium Order");
        System.out.println("3. Add food items");
        System.out.println("4. Remove items");
        System.out.println("5. Apply coupon");
        System.out.println("6. View all orders");
        System.out.println("7. Calculate bills (polymorphism demo)");
        System.out.println("8. Update order status");
        System.out.println("9. Exit");
        System.out.println("==============================================");
    }

    private static Order selectOrder(Scanner scanner, List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No orders available.");
            return null;
        }

        System.out.println("Available Orders:");
        for (Order order : orders) {
            System.out.println("- ID: " + order.getOrderId() + " | Type: " + order.getClass().getSimpleName());
        }

        System.out.print("Enter orderId: ");
        int orderId = readInt(scanner);

        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }

        System.out.println("Order not found.");
        return null;
    }

    private static FoodCategory readCategory(Scanner scanner) {
        while (true) {
            System.out.println("Select category:");
            FoodCategory[] categories = FoodCategory.values();
            for (int i = 0; i < categories.length; i++) {
                System.out.println((i + 1) + ". " + categories[i]);
            }

            System.out.print("Enter choice: ");
            int choice = readInt(scanner);
            if (choice >= 1 && choice <= categories.length) {
                return categories[choice - 1];
            }
            System.out.println("Invalid category choice.");
        }
    }

    private static OrderStatus readOrderStatus(Scanner scanner) {
        while (true) {
            System.out.println("Select order status:");
            OrderStatus[] statuses = OrderStatus.values();
            for (int i = 0; i < statuses.length; i++) {
                System.out.println((i + 1) + ". " + statuses[i]);
            }

            System.out.print("Enter choice: ");
            int choice = readInt(scanner);
            if (choice >= 1 && choice <= statuses.length) {
                return statuses[choice - 1];
            }
            System.out.println("Invalid status choice.");
        }
    }

    private static void viewOrdersByType(Scanner scanner, OrderService orderService, List<Order> orders) {
        System.out.println("View options:");
        System.out.println("1. All Orders");
        System.out.println("2. Only Regular Orders");
        System.out.println("3. Only Premium Orders");
        System.out.print("Enter choice: ");
        int viewChoice = readInt(scanner);

        List<Order> result;
        switch (viewChoice) {
            case 1:
                result = orders;
                break;
            case 2:
                result = orderService.getOrdersByType(orders, RegularOrder.class);
                break;
            case 3:
                result = orderService.getOrdersByType(orders, PremiumOrder.class);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (result.isEmpty()) {
            System.out.println("No matching orders found.");
            return;
        }

        for (Order order : result) {
            orderService.printOrderSummary(order);
        }
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static double readDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print("Please enter a valid decimal value: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
