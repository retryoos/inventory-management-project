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

    // View all stock
    public void viewAllProducts(Inventory inventory) {
        inventory.view(connection);
    }

    // Search specific product based on productName (maybe consider productId)
    public void searchProduct(String productName, Inventory inventory) {
        inventory.search(connection, productName);
    }

    // Generates sales report csv for a specific employee
    public void generateEmployeesSalesReport(int employeeId, Sales sales) {
        sales.generateReport(connection, employeeId);
    }

    // Generates a sales report csv for all sales from all employees
    public void generateAllSalesReport(Sales sales) {
        sales.generateAllSalesReport(connection);
    }

    // Add sale
    public void addSale(String productName, int qty, double price, int employeeId, int productId, Sales sales) {
        sales.add(connection, productName, qty, price, employeeId, productId);
    }

    public void cancelSale(int saleId, Sales sales) {
        sales.cancel(connection, saleId);
    }

    public void removeEmployee(int employeeId, EmployeeManage employeeManage) {
        employeeManage.remove(connection, employeeId);
    }

    public void generateAllEmployeesReport(EmployeeManage employeeManage) {
        employeeManage.viewAll(connection);
    }

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