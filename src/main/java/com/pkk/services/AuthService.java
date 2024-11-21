package com.pkk.services;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class AuthService {
    
    // Authenticate user and return UserDetails (role and ID)
    public static UserDetails authenticateUser(String username, String password) {
        String query = "SELECT id, password_hash, role FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPasswordHash = rs.getString("password_hash");
                String role = rs.getString("role");
                int id = rs.getInt("id"); // Retrieving the user ID

                if (BCrypt.checkpw(password, storedPasswordHash)) {
                    System.out.println("Login successful. Role: " + role);
                    return new UserDetails(id, role); // Returning id along with role
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during authentication: " + e.getMessage());
        }
        System.out.println("Invalid username or password.");
        return null; // Return null if authentication fails
    }

    // Register a new user (unauthenticated users can register as employees, only managers can create managers)
    public static boolean registerUser(String currentRole, String newUsername, String newPassword, String newRole) {
        // Ensure the new role is valid (only 'manager' or 'employee')
        if (!"manager".equals(newRole) && !"employee".equals(newRole)) {
            System.out.println("Invalid role. Must be 'manager' or 'employee'.");
            return false;
        }

        // Null (unauthenticated) users can only register themselves as employees
        if (currentRole == null) {
            if (!"employee".equals(newRole)) {
                System.out.println("Unauthenticated users can only register as employees.");
                return false;
            }
        } else if ("employee".equals(currentRole)) {
            // Employees cannot register new users
            System.out.println("Employees cannot register new users.");
            return false;
        } else if ("manager".equals(currentRole)) {
            // Only managers can register other managers
            if ("manager".equals(newRole)) {
                System.out.println("Manager is creating a new manager.");
            }
        } else {
            System.out.println("Invalid current role.");
            return false;
        }

        // Check if the username is already taken
        if (isUsernameTaken(newUsername)) {
            System.out.println("Username is already taken.");
            return false;
        }

        // Hash the password
        String passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Insert the new user into the database
        String query = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, passwordHash);
            stmt.setString(3, newRole);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User registered successfully.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
        return false;
    }


    // Check if a username is already taken
    private static boolean isUsernameTaken(String username) {
        String query = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking username availability: " + e.getMessage());
        }
        return false;
    }

    // Simple class to store user details (id, role)
    public static class UserDetails {
        public final int id;
        public final String role;

        public UserDetails(int id, String role) {
            this.id = id;
            this.role = role;
        }
    }

}
  
