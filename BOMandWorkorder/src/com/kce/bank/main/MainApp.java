package com.kce.bank.main;

import com.kce.bank.exception.InsufficientStockException;
import com.kce.bank.exception.InvalidOperationException;
import com.kce.bank.model.*;
import com.kce.bank.service.MRPService;

import java.util.*;

/**
 * Console menu driving the MRP system.
 * Run this class (Main method) in Eclipse.
 */
public class MainApp {
    private static final Scanner SC = new Scanner(System.in);
    private static MRPService service;

    public static void main(String[] args) {
        System.out.println("=== Simple MRP Console App ===");
        Warehouse wh = new Warehouse("MainWarehouse");
        service = new MRPService(wh);

        // seed sample data
        seedSampleData();

        boolean running = true;
        while (running) {
            printMenu();
            int ch = readInt("Choose option: ");
            try {
                switch (ch) {
                    case 1: addProduct(); break;
                    case 2: defineBOM(); break;
                    case 3: createWorkOrder(); break;
                    case 4: issueMaterials(); break;
                    case 5: reportProduction(); break;
                    case 6: stockSummary(); break;
                    case 7: listWorkOrders(); break;
                    case 8: running = false; break;
                    default: System.out.println("Invalid option."); break;
                }
            } catch (InvalidOperationException | InsufficientStockException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Exiting. Goodbye!");
        SC.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Add Product");
        System.out.println("2. Define BOM");
        System.out.println("3. Create Work Order (reserves materials)");
        System.out.println("4. Issue Materials (for a WO)");
        System.out.println("5. Report Production");
        System.out.println("6. Stock Summary");
        System.out.println("7. List Work Orders");
        System.out.println("8. Exit");
    }

    private static void addProduct() {
        String code = readStr("Product code (eg C001): ");
        String name = readStr("Product name: ");
        int stock = readInt("Initial stock qty: ");
        try {
            service.addProduct(code, name, stock);
            System.out.println("Added product " + code);
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add product: " + e.getMessage());
        }
    }

    private static void defineBOM() throws InvalidOperationException {
        String prodCode = readStr("Finished product code: ");
        if (service.getProduct(prodCode) == null) {
            System.out.println("Product doesn't exist. Add it first.");
            return;
        }
        List<BOMItem> items = new ArrayList<>();
        while (true) {
            String comp = readStr("Component code (blank to finish): ");
            if (comp.isBlank()) break;
            Product p = service.getProduct(comp);
            if (p == null) {
                System.out.println("Component not found. Add product first.");
                continue;
            }
            int qty = readInt("Qty per finished unit: ");
            items.add(new BOMItem(p, qty));
        }
        if (items.isEmpty()) {
            System.out.println("No BOM items specified.");
            return;
        }
        service.defineBOM(prodCode, items);
        System.out.println("BOM defined for " + prodCode);
    }

    private static void createWorkOrder() throws InvalidOperationException, InsufficientStockException {
        String prodCode = readStr("Product to produce: ");
        int qty = readInt("Quantity to produce: ");
        WorkOrder wo = service.createWorkOrder(prodCode, qty);
        System.out.println("Created WorkOrder: " + wo.getId() + " (materials reserved).");
    }

    private static void issueMaterials() throws InvalidOperationException, InsufficientStockException {
        listWorkOrders();
        String woId = readStr("WorkOrder id to issue materials for: ");
        WorkOrder wo = findWorkOrder(woId);
        if (wo == null) {
            System.out.println("WO not found.");
            return;
        }
        System.out.println("1. Issue all per BOM\n2. Issue selected component");
        int opt = readInt("Choose: ");
        if (opt == 1) {
            List<MaterialIssue> issued = service.issueMaterialsForWorkOrder(woId);
            System.out.println("Issued materials:");
            issued.forEach(m -> System.out.println("  " + m));
        } else {
            String comp = readStr("Component code: ");
            int qty = readInt("Qty to issue: ");
            MaterialIssue mi = service.issueMaterial(woId, comp, qty);
            System.out.println("Issued: " + mi);
        }
    }

    private static void reportProduction() throws InvalidOperationException {
        listWorkOrders();
        String woId = readStr("WorkOrder id to report production for: ");
        WorkOrder wo = findWorkOrder(woId);
        if (wo == null) {
            System.out.println("WO not found.");
            return;
        }
        int qty = readInt("Quantity produced (<= WO qty): ");
        ProductionReport pr = service.reportProduction(woId, qty);
        System.out.println("Production reported: " + pr);
    }

    private static void stockSummary() {
        System.out.println(service.warehouseSummary());
    }

    private static void listWorkOrders() {
        System.out.println("\nWork Orders:");
        for (WorkOrder w : service.allWorkOrders()) {
            System.out.println(w);
        }
    }

    private static WorkOrder findWorkOrder(String id) {
        for (WorkOrder w : service.allWorkOrders()) if (w.getId().equals(id)) return w;
        return null;
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = SC.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static String readStr(String prompt) {
        System.out.print(prompt);
        return SC.nextLine().trim();
    }

    private static void seedSampleData() {
        // Add components and finished product
        service.addProduct("C001", "Component-1", 500);
        service.addProduct("C002", "Component-2", 300);
        service.addProduct("FG01", "Finished-Good-1", 10);

        // Define BOM for FG01: needs C001 x2, C002 x1
        List<BOMItem> items = new ArrayList<>();
        items.add(new BOMItem(service.getProduct("C001"), 2));
        items.add(new BOMItem(service.getProduct("C002"), 1));
        try {
            service.defineBOM("FG01", items);
        } catch (InvalidOperationException e) {
            System.out.println("Seed BOM failed: " + e.getMessage());
        }

        System.out.println("Seeded sample products and BOM.");
    }
}
