package com.kce.bank.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class BOM {
    private final Product finishedProduct;
    private final List<BOMItem> items = new ArrayList<>();

    public BOM(Product finishedProduct) {
        this.finishedProduct = finishedProduct;
    }

    public Product getFinishedProduct() { return finishedProduct; }
    public void addItem(BOMItem item) { items.add(item); }
    public List<BOMItem> getItems() { return Collections.unmodifiableList(items); }
    public String toString() {
        StringBuilder sb = new StringBuilder("BOM for " + finishedProduct + ":\n");
        for (BOMItem it : items) {
            sb.append("   - ").append(it).append("\n");
        }
        return sb.toString();
    }
}
