package com.ppk.functionalities;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeManage {
    
    public void remove(Connection conn, int employeeId) {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) { // Use 'connection' here
            stmt.setInt(1, employeeId);
            stmt.executeUpdate();
            System.out.println("Employee with id [" + employeeId + "] removed from users.");
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
    }

    public void viewAll(Connection conn) {
        String query = "SELECT id, username, created_at FROM users WHERE role = 'employee'";
    
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            // Write to CSV file
            try (FileWriter writer = new FileWriter("users_report.csv")) {
                // Write the CSV header
                writer.append("Employee ID, Username, Created At\n");
    
                // Loop through the ResultSet and write each row to the CSV file
                while (rs.next()) {
                    writer.append(rs.getInt("id") + ",");
                    writer.append(rs.getString("username") + ",");
                    writer.append(rs.getTimestamp("created_at") + "\n");
                }
                
                System.out.println("Users report generated: users_report.csv");
    
            } catch (IOException e) {
                System.out.println("Error writing users report: " + e.getMessage());
            }
    
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }
    

}
