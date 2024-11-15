package app.users;

import app.fuctionalities.StockUpdate;

public class Employee extends User {
    private int employeeId;

    // Constructor
    public Employee(String username, String password, int employeeId) {
        super(username, password);
        this.employeeId = employeeId;
    }

    public int getEmployeeId() {
        return employeeId;
    } 

    @Override
    public String getRole() {
        return "Sales Employee";
    }

    public void sellProduct(String productName, int qty, StockUpdate stockUpdate) {
        // Logic to deduct stock from db of stock and to create a new sale entry 
        // We will have 2 tables on for sales and one for stock
        // Maybe break this down in to other private methods one to deduct stock and one to create sale entry
        System.out.println("Employee " + employeeId + " sold " + qty + " of " + productName);
    }
}