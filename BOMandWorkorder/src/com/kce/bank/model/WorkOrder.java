package com.kce.bank.model;
import com.kce.bank.util.IdGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class WorkOrder {
    public enum Status { CREATED, MATERIAL_RESERVED, MATERIAL_ISSUED, IN_PRODUCTION, COMPLETED }
    private final String id;
    private final Product product;
    private final int quantity;
    private Status status;
    private final List<MaterialIssue> materialIssues = new ArrayList<>();
    private ProductionReport productionReport; 
    public WorkOrder(Product product, int quantity) {
        this.id = IdGenerator.next("WO");
        this.product = product;
        this.quantity = quantity;
        this.status = Status.CREATED;}
    public String getId() { return id; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public synchronized Status getStatus() { return status; }
    public synchronized void setStatus(Status status) { this.status = status; }
    public void addMaterialIssue(MaterialIssue mi) { materialIssues.add(mi); }
    public List<MaterialIssue> getMaterialIssues() { return Collections.unmodifiableList(materialIssues); }
    public void setProductionReport(ProductionReport pr) { this.productionReport = pr; }
    public ProductionReport getProductionReport() { return productionReport; }
    public String simple() {
        return id + " [" + product.getCode() + " x" + quantity + "]";
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(simple()).append(" Status=").append(status).append("\n");
        if (!materialIssues.isEmpty()) {
            sb.append("  Material Issues:\n");
            for (MaterialIssue mi : materialIssues) sb.append("    ").append(mi).append("\n");
        }
        if (productionReport != null) sb.append("  Production: ").append(productionReport).append("\n");
        return sb.toString();
    }
}
