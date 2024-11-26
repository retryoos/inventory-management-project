package app.users;

import app.fuctionalities.Inventory;
import app.fuctionalities.Sales;
import app.services.DatabaseConnection;
import app.services.AuthService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Manager extends User {
    private Connection connection;
    private int id;

    // Constructor
    public Manager(String username, String password, int id) {
        super(username, password);
        this.id = id;
        try {
            // Initialize connection when the manager is logged in
            this.connection = DatabaseConnection.connect();
            System.out.println("Database connection established for Manager " + id);
        } catch (SQLException e) {
            System.out.println("Error establishing database connection: " + e.getMessage());
        }
    }

    // Accessor method for id
    public int getId() {
        return id;
    }

    @Override
    public String getRole() {
        return "Manager";
    }

    public void addProduct(String productName, double price, int qty, Inventory inventory) {
        inventory.add(connection, productName, qty, price);
    }

    // make either productName or productId from table
    public void removeProduct(int productId, Inventory inventory) {
        inventory.remove(connection, productId);
    }

    // Consider passing product id as well to match with product we want to update
    public void updateProduct(int productId, String productName, int qty, double price, Inventory inventory) {
        inventory.update(connection, productName, qty, price);
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
    public void generateSalesReport(int employeeId, String timeFrame, Sales sales) {
        sales.generateReport(connection, employeeId, timeFrame);
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