package com.ppk.users;

import com.ppk.functionalities.Inventory;
import com.ppk.functionalities.Sales;
import com.ppk.services.DatabaseConnection;
import com.ppk.services.AuthService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Employee extends User {
    private int employeeId;
    private Connection connection;

    // Constructor
    public Employee(String username, String password, int employeeId, Connection conn) {
        super(username, password);
        this.employeeId = employeeId;
        if (conn == null) {
            System.out.println("Connection is null! Unable to establish connection.");
        } else {
            this.connection = conn;
            System.out.println("Database connection established for Employee " + employeeId);
        }
    }

    // Getter for employeeId
    public int getEmployeeId() {
        return employeeId;
    }

    // Inventory Methods
    // View all inventory
    public void viewAll(Inventory inventory) {
        inventory.viewAll(connection);
    }

    // Export all inventory
    public void exportInventory(Inventory inventory) {
        inventory.exportAll(connection);
    }

    // Search for item in inventory
    public void searchItem(String productName, Inventory inventory) {
        inventory.search(connection, productName);
    }


    // Sales Methods
    // View all sales of employee
    public void viewAllSales(int employeeId, Sales sales) {
        sales.viewAllSalesEmployee(connection, employeeId);
    }

    // Generate Employee Specific Report
    public void generateReport(int employeeId, Sales sales) {
        sales.generateReport(connection, employeeId);
    }

    // Add a sale and automatically deducts the quantity from the inventory
    public void addSale(String productName, int qty, double price, int employeeId, int productId, Sales sales) {
        sales.add(connection, productName, qty, price, employeeId, productId);
    }

    // Close session for usage upon logout
    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {  // Ensure the connection is open before closing it
                    connection.close();
                    System.out.println("Database connection closed for Employee " + employeeId);
                } else {
                    System.out.println("Database connection is already closed for Employee " + employeeId);
                }
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        } else {
            System.out.println("No connection to close for Employee " + employeeId);
        }
    }
}