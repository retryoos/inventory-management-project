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