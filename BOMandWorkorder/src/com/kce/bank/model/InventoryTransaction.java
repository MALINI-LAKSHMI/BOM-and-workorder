package com.kce.bank.model;
import java.time.LocalDateTime;
public abstract class InventoryTransaction {
    private final String id;
    private final LocalDateTime timestamp;
    protected InventoryTransaction(String id) {
        this.id = id;
        this.timestamp = LocalDateTime.now();
    }
    public String getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public abstract String summary();
}
