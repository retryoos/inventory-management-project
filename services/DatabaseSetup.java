package app.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSetup {

    public static void setupInitialManager() {
        try (Connection conn = DatabaseConnection.connect()) {
            // Check if there is already a manager in the users table
            String checkQuery = "SELECT COUNT(*) FROM users WHERE role = 'manager'";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int managerCount = rs.getInt(1);

            // If no manager exists, create an initial manager account
            if (managerCount == 0) {
                String createManagerQuery = "INSERT INTO users (username, password_hash, role, date_created) VALUES (?, ?, 'manager', NOW())";
                PreparedStatement createStmt = conn.prepareStatement(createManagerQuery);
                createStmt.setString(1, "admin"); // Set your initial username (admin or custom)
                createStmt.setString(2, "admin123"); // Set your initial password (hashed later)
                createStmt.executeUpdate();
                System.out.println("Initial manager account created with username: admin and password: admin123");
            }
        } catch (SQLException e) {
            System.out.println("Error during database setup: " + e.getMessage());
        }
    }
}
