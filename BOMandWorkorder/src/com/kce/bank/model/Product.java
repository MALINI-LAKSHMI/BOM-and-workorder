package com.kce.bank.model;

/**
 * Represents a product or component.
 */
public class Product {
    private final String code;
    private String name;

    public Product(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() { return code; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return name + " [" + code + "]";
    }
}

