package com.ppk.users;

import com.ppk.functionalities.Inventory;
import com.ppk.functionalities.Sales;
import com.ppk.functionalities.EmployeeManage;

import com.ppk.services.DatabaseConnection;
import com.ppk.services.AuthService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Manager extends User {
    private Connection connection;
    private int id;

    // Constructor
    public Manager(String username, String password, int id, Connection conn) {
        super(username, password);
        this.id = id;
        if (conn == null) {
            System.out.println("Connection is null! Unable to establish connection.");
        } else {
            this.connection = conn;
            System.out.println("Database connection established for Manager " + id);
        }
    }

    // Accessor method for id
    public int getId() {
        return id;
    }

    // Inventory methods
    // Adds product to inventory
    public void addProduct(String productName, int quantity, double price, Inventory inventory) {
        inventory.add(connection, productName, quantity, price);
    }

    // Removes product from inventory based on productId
    public void removeProduct(int productId, Inventory inventory) {
        inventory.remove(connection, productId);
    }

    // Updates product in inventory based on productId
    public void updateProduct(int productId, String productName, int qty, double price, Inventory inventory) {
        inventory.update(connection, productId, productName, qty, price);
    }

    // Search specific product based on productName in inventory
    public void searchProduct(String productName, Inventory inventory) {
        inventory.search(connection, productName);
    }
    
    // View all stock in inventory
    public void viewAll(Inventory inventory) {
        inventory.viewAll(connection);
    }

    // View all stock in csv report
    public void exportAll(Inventory inventory) {
        inventory.exportAll(connection);
    }


    // Sales methods
    // Generates sales report csv for a specific employee based on employeeId
    public void generateEmployeesSalesReport(int employeeId, Sales sales) {
        sales.generateReport(connection, employeeId);
    }

    // Generates a sales report csv for all sales from all employees
    public void generateAllSalesReport(Sales sales) {
        sales.generateAllSalesReport(connection);
    }

    // View all sales in the database
    public void viewAllSales(Sales sales) {
        sales.viewAllSalesManager(connection);
    }

    // Add sale and automatically deducts quantity from inventory
    public void addSale(String productName, int qty, double price, int employeeId, int productId, Sales sales) {
        sales.add(connection, productName, qty, price, employeeId, productId);
    }

    // Cancel sale based on saleId
    public void cancelSale(int saleId, Sales sales) {
        sales.cancel(connection, saleId);
    }

    // Employee management methods
    // Remove employee from users table (delete)
    public void removeEmployee(int employeeId, EmployeeManage employeeManage) {
        employeeManage.remove(connection, employeeId);
    }

    // Generate a csv with all employees
    public void generateAllEmployeesReport(EmployeeManage employeeManage) {
        employeeManage.viewAll(connection);
    }

    // Close connection for usage upon logging out
    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {  // Ensure the connection is open before closing it
                    connection.close();
                    System.out.println("Database connection closed for Manager " + id);
                } else {
                    System.out.println("Database connection is already closed for Manager " + id);
                }
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        } else {
            System.out.println("No connection to close for Manager " + id);
        }
    }
    
}