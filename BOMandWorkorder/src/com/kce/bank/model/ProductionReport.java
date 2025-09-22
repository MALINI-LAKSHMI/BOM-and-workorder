package com.kce.bank.model;
import com.kce.bank.util.IdGenerator;
public class ProductionReport extends InventoryTransaction {
    private final WorkOrder workOrder;
    private final int producedQuantity;
    public ProductionReport(WorkOrder workOrder, int producedQuantity) {
        super(IdGenerator.next("PR"));
        this.workOrder = workOrder;
        this.producedQuantity = producedQuantity;
    }
    public WorkOrder getWorkOrder() { return workOrder; }
    public int getProducedQuantity() { return producedQuantity; }
    public String summary() {
        return String.format("%s: Produced %d units for %s", getId(), producedQuantity, workOrder.simple());
    }
    public String toString() { return summary(); }
}
