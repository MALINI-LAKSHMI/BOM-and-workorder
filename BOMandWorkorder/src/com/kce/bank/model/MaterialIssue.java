package com.kce.bank.model;
import com.kce.bank.util.IdGenerator;
public class MaterialIssue extends InventoryTransaction {
    private final WorkOrder workOrder;
    private final Warehouse warehouse;
    private final Product component;
    private final int quantityIssued;
    public MaterialIssue(WorkOrder workOrder, Warehouse warehouse, Product component, int quantityIssued) {
        super(IdGenerator.next("MI"));
        this.workOrder = workOrder;
        this.warehouse = warehouse;
        this.component = component;
        this.quantityIssued = quantityIssued;
    }
    public WorkOrder getWorkOrder() { return workOrder; }
    public Warehouse getWarehouse() { return warehouse; }
    public Product getComponent() { return component; }
    public int getQuantityIssued() { return quantityIssued; }
    public String summary() {
        return String.format("%s: Issued %d of %s from %s for %s",
                getId(), quantityIssued, component.getCode(), warehouse.getName(), workOrder.simple());
    }
    public String toString() { return summary(); }
}
