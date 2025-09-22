package com.kce.bank.service;

import com.kce.bank.exception.InsufficientStockException;
import com.kce.bank.exception.InvalidOperationException;
import com.kce.bank.model.*;
import java.util.*;

/**
 * Service layer that implements required business logic.
 */
public class MRPService {
    private final Map<String, Product> products = new HashMap<>();
    private final Map<String, BOM> boms = new HashMap<>();
    private final Map<String, WorkOrder> workOrders = new LinkedHashMap<>();
    private final Warehouse warehouse;

    public MRPService(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    // Product management
    public void addProduct(String code, String name, int initialStock) {
        if (products.containsKey(code)) throw new IllegalArgumentException("Product code exists: " + code);
        Product p = new Product(code, name);
        products.put(code, p);
        if (initialStock > 0) warehouse.addStock(p, initialStock);
    }

    public Product getProduct(String code) { return products.get(code); }
    public Collection<Product> allProducts() { return Collections.unmodifiableCollection(products.values()); }

    // BOM management
    public void defineBOM(String productCode, List<BOMItem> items) throws InvalidOperationException {
        Product p = products.get(productCode);
        if (p == null) throw new InvalidOperationException("Product not found: " + productCode);
        BOM bom = new BOM(p);
        for (BOMItem it : items) bom.addItem(it);
        boms.put(productCode, bom);
    }

    public BOM getBOM(String productCode) { return boms.get(productCode); }

    // Work Order creation — reserves components per BOM (available -> reserved)
    public WorkOrder createWorkOrder(String productCode, int qty) throws InvalidOperationException, InsufficientStockException {
        Product prod = products.get(productCode);
        if (prod == null) throw new InvalidOperationException("Product not found: " + productCode);
        BOM bom = boms.get(productCode);
        if (bom == null) throw new InvalidOperationException("BOM not defined for: " + productCode);
        WorkOrder wo = new WorkOrder(prod, qty);
        // Reserve components
        for (BOMItem item : bom.getItems()) {
            int required = item.getQtyPerProduct() * qty;
            warehouse.reserve(item.getComponent().getCode(), required);
        }
        wo.setStatus(WorkOrder.Status.MATERIAL_RESERVED);
        workOrders.put(wo.getId(), wo);
        return wo;
    }

    // Issue materials — moves reserved -> consumed and records MaterialIssue objects
    public MaterialIssue issueMaterial(String woId, String componentCode, int qty)
            throws InvalidOperationException, InsufficientStockException {
        WorkOrder wo = workOrders.get(woId);
        if (wo == null) throw new InvalidOperationException("WorkOrder not found: " + woId);
        if (wo.getStatus() == WorkOrder.Status.CREATED) throw new InvalidOperationException("Materials not reserved yet.");
        Product comp = products.get(componentCode);
        if (comp == null) throw new InvalidOperationException("Component not found: " + componentCode);
        // Issue reserved quantity
        warehouse.issueReserved(componentCode, qty);
        MaterialIssue mi = new MaterialIssue(wo, warehouse, comp, qty);
        wo.addMaterialIssue(mi);
        // If at least one material was issued, transition to MATERIAL_ISSUED
        if (wo.getStatus() == WorkOrder.Status.MATERIAL_RESERVED) wo.setStatus(WorkOrder.Status.MATERIAL_ISSUED);
        return mi;
    }

    // Bulk issue based on BOM (issue all required components for the work order)
    public List<MaterialIssue> issueMaterialsForWorkOrder(String woId)
            throws InvalidOperationException, InsufficientStockException {
        WorkOrder wo = workOrders.get(woId);
        if (wo == null) throw new InvalidOperationException("WorkOrder not found: " + woId);
        BOM bom = boms.get(wo.getProduct().getCode());
        if (bom == null) throw new InvalidOperationException("BOM not defined for: " + wo.getProduct().getCode());
        List<MaterialIssue> issued = new ArrayList<>();
        for (BOMItem item : bom.getItems()) {
            int required = item.getQtyPerProduct() * wo.getQuantity();
            // This will throw InsufficientStockException if reserved < required
            warehouse.issueReserved(item.getComponent().getCode(), required);
            MaterialIssue mi = new MaterialIssue(wo, warehouse, item.getComponent(), required);
            wo.addMaterialIssue(mi);
            issued.add(mi);
        }
        wo.setStatus(WorkOrder.Status.MATERIAL_ISSUED);
        return issued;
    }

    // Report production — requires materials issued; increases finished goods stock.
    public ProductionReport reportProduction(String woId, int producedQty) throws InvalidOperationException {
        WorkOrder wo = workOrders.get(woId);
        if (wo == null) throw new InvalidOperationException("WorkOrder not found: " + woId);
        if (wo.getStatus() != WorkOrder.Status.MATERIAL_ISSUED)
            throw new InvalidOperationException("Materials must be issued before production reporting.");
        if (producedQty <= 0 || producedQty > wo.getQuantity())
            throw new InvalidOperationException("Produced quantity invalid.");
        // Add finished goods to warehouse available stock
        warehouse.addStock(wo.getProduct(), producedQty);
        ProductionReport pr = new ProductionReport(wo, producedQty);
        wo.setProductionReport(pr);
        wo.setStatus(WorkOrder.Status.COMPLETED);
        return pr;
    }

    // Helpers for main UI
    public Collection<WorkOrder> allWorkOrders() { return Collections.unmodifiableCollection(workOrders.values()); }

    public String warehouseSummary() { return warehouse.stockSummary(); }
}
