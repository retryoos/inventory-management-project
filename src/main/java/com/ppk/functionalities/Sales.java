package com.ppk.functionalities;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sales {

    // Consider timeframe string param to parse
    public void generateReport(Connection conn, int employeeId) {
        String query = "SELECT * FROM sales WHERE employee_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the employeeId parameter
            stmt.setInt(1, employeeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                try (FileWriter writer = new FileWriter("sales_report_" + employeeId + ".csv")) {
                    writer.append("Sale ID, Product Name, Quantity, Price, Total Amount, Date\n");
                    while (rs.next()) {
                        writer.append(rs.getInt("id") + ",");
                        writer.append(rs.getString("product_name") + ",");
                        writer.append(rs.getInt("quantity_sold") + ",");
                        writer.append(rs.getDouble("price") + ",");
                        writer.append(rs.getDouble("total_amount") + ",");
                        writer.append(rs.getDate("sale_date") + "\n");
                    }
                    System.out.println("Sales report generated: sales_report_" + employeeId + ".csv");
                } catch (SQLException | IOException e) {
                    System.out.println("Error generating sales report: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("Error executing query: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error preparing statement: " + e.getMessage());
        }
    }

    // Generate sales report for all employees that the manager can access
    public void generateAllSalesReport(Connection conn) {
        String query = "SELECT * FROM sales";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            try (FileWriter writer = new FileWriter("all_sales_report.csv")) {
                writer.append("Sale ID, Product Name, Quantity, Price, Total Amount, Employee ID, Date\n");
                while (rs.next()) {
                    writer.append(rs.getInt("id") + ",");
                    writer.append(rs.getString("product_name") + ",");
                    writer.append(rs.getInt("quantity_sold") + ",");
                    writer.append(rs.getDouble("price") + ",");
                    writer.append(rs.getDouble("total_amount") + ",");
                    writer.append(rs.getInt("employee_id") + ",");
                    writer.append(rs.getDate("sale_date") + "\n");
                }
                System.out.println("All sales report generated: all_sales_report.csv");
            } catch (IOException e) {
                System.out.println("Error writing sales report: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }
    
    // add one more parameter of productId to send to updateQuantity
    public void add(Connection conn, String productName, int qty, double price, int employeeId, int productId) {
        String insertSaleQuery = "INSERT INTO sales (product_name, quantity_sold, price, employee_id) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(insertSaleQuery)) {
            // Add sale to sales table
            stmt.setString(1, productName);
            stmt.setInt(2, qty);
            stmt.setDouble(3, price);
            stmt.setInt(4, employeeId);
            stmt.executeUpdate();
            System.out.println("Sale added successfully!");
    
            // Call updateQuantity method to update the inventory with the provided productId
            updateQuantity(conn, productId, qty); // Pass the productId and quantity sold
        } catch (SQLException e) {
            System.out.println("Error adding sale: " + e.getMessage());
        }
    }

    private void updateQuantity(Connection conn, int productId, int quantitySold) {
        String selectQuery = "SELECT quantity FROM inventory WHERE id = ?";
        String updateQuery = "UPDATE inventory SET quantity = ? WHERE id = ?";
        String deleteQuery = "DELETE FROM inventory WHERE id = ?";
    
        try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
            // Get the current quantity from the inventory table
            selectStmt.setInt(1, productId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    int currentQuantity = rs.getInt("quantity");
                    int newQuantity = currentQuantity - quantitySold;
    
                    if (newQuantity > 1) {
                        // Update the inventory if the new quantity is greater than 1
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, newQuantity);
                            updateStmt.setInt(2, productId);
                            updateStmt.executeUpdate();
                            System.out.println("Quantity updated successfully!");
                        } catch (SQLException e) {
                            System.out.println("Error updating quantity: " + e.getMessage());
                        }
                    } else if (newQuantity == 1 || newQuantity == 0) {
                        // Delete the product if only 1 item is left
                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                            deleteStmt.setInt(1, productId);
                            deleteStmt.executeUpdate();
                            System.out.println("Product deleted from inventory.");
                        } catch (SQLException e) {
                            System.out.println("Error deleting product: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Not enough quantity to complete sale.");
                    }
                } else {
                    System.out.println("Product not found in inventory.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching product data: " + e.getMessage());
        }
    }

    public void cancel(Connection conn, int saleId) {
        String query = "DELETE FROM sales WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, saleId);
            stmt.executeUpdate();
            System.out.println("Sale with ID [" + saleId + "] cancelled successfully.");
        } catch (SQLException e) {
            System.out.println("Error cancelling sale: " + e.getMessage());
        }
    }

}
