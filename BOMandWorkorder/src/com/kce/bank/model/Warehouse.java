package com.kce.bank.model;

import com.kce.bank.exception.InsufficientStockException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Warehouse maintains available and reserved stock per product code.
 */
public class Warehouse {
    private final String name;
    private final Map<String, Integer> available = new HashMap<>();
    private final Map<String, Integer> reserved = new HashMap<>();

    public Warehouse(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    // Add stock (receiving finished goods or restocking)
    public synchronized void addStock(Product p, int qty) {
        if (qty <= 0) return;
        available.put(p.getCode(), available.getOrDefault(p.getCode(), 0) + qty);
    }

    // Reserve stock: move from available -> reserved. May throw if not enough.
    public synchronized void reserve(String productCode, int qty) throws InsufficientStockException {
        int avail = available.getOrDefault(productCode, 0);
        if (qty > avail) throw new InsufficientStockException("Not enough available stock to reserve for " + productCode);
        available.put(productCode, avail - qty);
        reserved.put(productCode, reserved.getOrDefault(productCode, 0) + qty);
    }

    // Issue reserved stock: move from reserved -> consumed (removed). May throw if not enough reserved.
    public synchronized void issueReserved(String productCode, int qty) throws InsufficientStockException {
        int res = reserved.getOrDefault(productCode, 0);
        if (qty > res) throw new InsufficientStockException("Not enough reserved stock to issue for " + productCode);
        reserved.put(productCode, res - qty);
        // consumed — not tracked in available
    }

    public synchronized int getAvailable(String productCode) { return available.getOrDefault(productCode, 0); }
    public synchronized int getReserved(String productCode) { return reserved.getOrDefault(productCode, 0); }

    public synchronized String stockSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Warehouse ").append(name).append(" Stock:\n");
        Set<String> keys = new java.util.TreeSet<>();
        keys.addAll(available.keySet());
        keys.addAll(reserved.keySet());
        sb.append(String.format("%-12s %-10s %-10s\n", "Product", "Available", "Reserved"));
        for (String k : keys) {
            sb.append(String.format("%-12s %-10d %-10d\n", k, available.getOrDefault(k, 0), reserved.getOrDefault(k, 0)));
        }
        return sb.toString();
    }
}
