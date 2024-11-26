package app.functionalities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * Consider adding public. infront of whereever we are querying the db
 */

public class Inventory {

    public void add(Connection conn, String productName, int quantity, double price) {
        if (productName.length() == 0 || quantity <= 0 || price <= 0) {
            System.out.println("Invalid input product cannot be added to the inventory.");
            return;
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productName);
            stmt.setInt(2, quantity);
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

    public void view(Connection conn) {
        String query = "SELECT * FROM inventory";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.executeQuery();
            System.out.println("Product Name | Quantity | Price");
            System.out.println("-------------------------------");
            while (stmt.getResultSet().next()) {
                System.out.println(stmt.getResultSet().getString("product_name") + " | " + stmt.getResultSet().getInt("quantity") + " | " + stmt.getResultSet().getDouble("price"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing inventory: " + e.getMessage());
        }
    }

    public void search(Connection conn, int productName) {
        String query = "SELECT * FROM inventory WHERE product_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productName);
            stmt.executeQuery();
            System.out.println("Product Name | Quantity | Price");
            System.out.println("-------------------------------");
            while (stmt.getResultSet().next()) {
                System.out.println(stmt.getResultSet().getString("product_name") + " | " + stmt.getResultSet().getInt("quantity") + " | " + stmt.getResultSet().getDouble("price"));
            }
        } catch (SQLException e) {
            System.out.println("Error searching inventory: " + e.getMessage());
        }
    }

    public void update(Connection conn, int productId, String productName, int qty, double price) {
        String query = "UPDATE inventory SET price = ?, quantity = ?, product_name = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, price);
            stmt.setInt(2, qty);
            stmt.setString(3, productName);
            stmt.setInt(4, productId);
            stmt.executeUpdate();
            System.out.println("Product with id [" + productId + "] updated in the inventory.");
        } catch (SQLException e) {
            System.out.println("Error updating product information: " + e.getMessage());
        }
    }
}