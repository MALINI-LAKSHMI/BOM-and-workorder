package com.kce.bank.exception;

/**
 * Thrown when stock is insufficient to reserve/issue.
 */
public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}
