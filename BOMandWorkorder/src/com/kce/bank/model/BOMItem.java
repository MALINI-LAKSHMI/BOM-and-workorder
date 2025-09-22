package com.kce.bank.model;
public class BOMItem {
    private final Product component;
    private final int qtyPerProduct;
    public BOMItem(Product component, int qtyPerProduct) {
        if (qtyPerProduct <= 0) throw new IllegalArgumentException("qtyPerProduct > 0");
        this.component = component;
        this.qtyPerProduct = qtyPerProduct;
    }
    public Product getComponent() { return component; }
    public int getQtyPerProduct() { return qtyPerProduct; }
    public String toString() {
        return component + " x" + qtyPerProduct;
    }
}
