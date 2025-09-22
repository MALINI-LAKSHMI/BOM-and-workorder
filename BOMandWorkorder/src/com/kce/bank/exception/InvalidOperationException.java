package com.kce.bank.exception;

/**
 * Thrown when an operation violates business rules.
 */
public class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }
}
