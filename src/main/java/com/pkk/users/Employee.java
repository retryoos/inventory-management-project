package com.pkk.users;

import com.pkk.functionalities.Inventory;
import com.pkk.functionalities.Sales;
import com.pkk.services.DatabaseConnection;
import com.pkk.services.AuthService;

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
        this.connection = conn; // Store the connection passed to the constructor
        System.out.println("Database connection established for Employee " + employeeId);
    }

    public int getEmployeeId() {
        return employeeId;
    }

    // Inventory Methods
    
    // View all inventory
    public void viewInventory(Inventory inventory) {
        inventory.view(connection);
    }

    // Search for item in inventory
    public void searchItem(String productName, Inventory inventory) {
        inventory.search(connection, productName);
    }

    // Sales Methods

    // Generate Employee Specific Report
    public void generateReport(int employeeId, Sales sales) {
        sales.generateReport(connection, employeeId);
    }

    // Make sale add to table
    public void addSale(String productName, int qty, double price, int employeeId, int productId, Sales sales) {
        sales.add(connection, productName, qty, price, employeeId, productId);
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed for Employee " + employeeId);
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}