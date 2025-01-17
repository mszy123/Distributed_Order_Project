package com.example.distributedordermatching;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderMatchingEngine {
    private final BlockingQueue<String> buyOrders = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> sellOrders = new LinkedBlockingQueue<>();

    public void addBuyOrder(String order) {
        try {
            buyOrders.put(order);
            System.out.printf("Added buy order: %s%n", order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("Failed to add buy order: %s%n", e.getMessage());
        }
    }

    public void addSellOrder(String order) {
        try {
            sellOrders.put(order);
            System.out.printf("Added sell order: %s%n", order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("Failed to add sell order: %s%n", e.getMessage());
        }
    }

    public void matchOrders() {
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            String buyOrder = buyOrders.poll();
            String sellOrder = sellOrders.poll();
            // Implement matching logic, e.g., based on price, quantity, etc.
            System.out.printf("Matched order: %s <--> %s%n", buyOrder, sellOrder);
        }
    }

    public static void main(String[] args) {
        OrderMatchingEngine engine = new OrderMatchingEngine();
        // Simulate adding orders
        engine.addBuyOrder("BUY,AAPL,10,150.00");
        engine.addSellOrder("SELL,AAPL,10,150.00");
        engine.matchOrders();
    }
}
