package com.pkk.users;

import com.pkk.functionalities.Inventory;
import com.pkk.functionalities.Sales;
import com.pkk.services.DatabaseConnection;
import com.pkk.services.AuthService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Manager extends User {
    private Connection connection;
    private int id;

    // Constructor
    public Manager(String username, String password, int id, Connection conn) {
        super(username, password);
        this.id = id;
        this.connection = conn; // Use the passed connection
        System.out.println("Database connection established for Manager " + id);
    }

    // Accessor method for id
    public int getId() {
        return id;
    }

    public void addProduct(String productName, int quantity, double price, Inventory inventory) {
        inventory.add(connection, productName, quantity, price);
    }

    // make either productName or productId from table
    public void removeProduct(int productId, Inventory inventory) {
        inventory.remove(connection, productId);
    }

    // Consider passing product id as well to match with product we want to update
    public void updateProduct(int productId, String productName, int qty, double price, Inventory inventory) {
        inventory.update(connection, productId, productName, qty, price);
    }

    // Consider optional parameter of specific category (once added)
    public void viewAllProducts(Inventory inventory) {
        inventory.view(connection);
    }

    // Search specific product based on productName (maybe consider productId)
    public void searchProduct(String productName, Inventory inventory) {
        inventory.search(connection, productName);
    }

    // Consider adding optional parameter for specific timeframe or from specific employee
    public void generateSalesReport(int employeeId, Sales sales) {
        sales.generateReport(connection, employeeId);
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed for Manager " + id);
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}