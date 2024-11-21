package app.users;

import app.fuctionalities.Inventory;
import app.functionalities.Sales;
import app.services.DatabaseConnection;
import app.services.AuthService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Employee extends User {
    private int employeeId;

    // Constructor
    public Employee(String username, String password, int employeeId) {
        super(username, password);
        this.employeeId = employeeId;
        try {
            // Initialize connection when the manager is logged in
            this.connection = DatabaseConnection.connect();
            System.out.println("Database connection established for Manager " + id);
        } catch (SQLException e) {
            System.out.println("Error establishing database connection: " + e.getMessage());
        }
    }

    public int getEmployeeId() {
        return employeeId;
    } 

    @Override
    public String getRole() {
        return "Employee";
    }

    
    public void addSale(String productName, int qty, double price, Sales sales) {
        try () {
            // decrease stock or remove item
        }
        sales.add(connection, productName, qty, price, employeeId);
    }

    public void cancelSale(int saleId, Sales sales) {
        try () {
            // increase stock or add iteam again
        }
        sales.cancel(connection, saleId);
    }

    // Employee Specific
    public void viewSales(Sales sales) {
        sales.view(connection, employeeId);
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