package com.ppk.functionalities;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Consider adding public. infront of whereever we are querying the db
 */

public class Inventory {

    public void add(Connection conn, String productName, int qty, double price) {
        if (productName.length() == 0 || qty <= 0 || price <= 0) {
            System.out.println("Invalid input product cannot be added to the inventory.");
            return;
        }
        String query = "INSERT INTO inventory (product_name, quantity, price) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productName);
            stmt.setInt(2, qty);
            stmt.setDouble(3, price);
            stmt.executeUpdate();
            System.out.println("Product [" + productName + "] added to the inventory.");
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
    }

    public void remove(Connection conn, int productId) {
        String query = "DELETE FROM inventory WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
            System.out.println("Product with id [" + productId + "] removed from the inventory.");
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
    }

    public void viewAll(Connection conn) {
        String query = "SELECT * FROM inventory";
        try (PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            System.out.println("ID | Product Name | Quantity | Price | Created At");
            System.out.println("-----------------------------------------------");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " + 
                                   rs.getString("product_name") + " | " + 
                                   rs.getInt("quantity") + " | " + 
                                   rs.getDouble("price") + " | " + 
                                   rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }

    public void exportAll(Connection conn) {
        String query = "SELECT * FROM inventory";
        try (PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            try (FileWriter writer = new FileWriter("inventory_report.csv")) {
                // Write the CSV header
                writer.append("ID, Product Name, Quantity, Price, Created At\n");
                // Loop through the ResultSet and write each row to the CSV file
                while (rs.next()) {
                    writer.append(rs.getInt("id") + ",");
                    writer.append(rs.getString("product_name") + ",");
                    writer.append(rs.getInt("quantity") + ",");
                    writer.append(rs.getDouble("price") + ",");
                    writer.append(rs.getTimestamp("created_at") + "\n");  // Assuming timestamp is stored
                }
                System.out.println("Inventory report generated: inventory_report.csv");
            } catch (IOException e) {
                System.out.println("Error writing inventory report: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }

    public void search(Connection conn, String productName) {
        String query = "SELECT * FROM inventory WHERE product_name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Add % wildcards around the productName to match substrings
            stmt.setString(1, "%" + productName + "%"); 
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Product Name | Quantity | Price");
                System.out.println("-------------------------------");
                while (rs.next()) {
                    System.out.println(rs.getString("product_name") + " | " + 
                                       rs.getInt("quantity") + " | " + 
                                       rs.getDouble("price"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching inventory: " + e.getMessage());
        }
    }    

    public void update(Connection conn, int productId, String productName, int qty, double price) {
        String checkQuery = "SELECT COUNT(*) FROM inventory WHERE id = ?";
        String updateQuery = "UPDATE inventory SET price = ?, quantity = ?, product_name = ? WHERE id = ?";
    
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            // Check if the product exists
            checkStmt.setInt(1, productId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) { // If COUNT(*) == 0, the product doesn't exist
                System.out.println("Error: Product with id [" + productId + "] does not exist in the inventory.");
                return; // Exit the method
            }
        } catch (SQLException e) {
            System.out.println("Error validating product existence: " + e.getMessage());
            return; // Exit if validation fails
        }
    
        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            // Update the product
            updateStmt.setDouble(1, price);
            updateStmt.setInt(2, qty);
            updateStmt.setString(3, productName);
            updateStmt.setInt(4, productId);
            updateStmt.executeUpdate();
            System.out.println("Product with id [" + productId + "] updated in the inventory.");
        } catch (SQLException e) {
            System.out.println("Error updating product information: " + e.getMessage());
        }
    }
}