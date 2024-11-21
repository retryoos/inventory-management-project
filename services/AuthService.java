package app.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class AuthService {
    
    // Method to authenticate user (either Manager or Employee)
    public static boolean authenticateUser(String username, String password) {
        String query = "SELECT password_hash, role FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPasswordHash = rs.getString("password_hash");
                String role = rs.getString("role");

                // Check if the entered password matches the hashed password
                if (BCrypt.checkpw(password, storedPasswordHash)) {
                    System.out.println("Login successful. Role: " + role);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
        }
        System.out.println("Invalid username or password.");
        return false;
    }

    // Register a new user (only managers can register users)
    public static boolean registerUser(String username, String password, String role, String currentRole) {
        // Check if the current logged-in user is a manager (only managers can register users)
        if (!currentRole.equals("manager")) {
            System.out.println("Only managers can register new users.");
            return false;
        }

        if (isUsernameTaken(username)) {
            System.out.println("Username is already taken.");
            return false;
        }

        // Hash the password before storing it in the database
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        // Check if the role is valid (can only be 'manager' or 'employee')
        if (!role.equals("manager") && !role.equals("employee")) {
            System.out.println("Invalid role. Only 'manager' or 'employee' can be registered.");
            return false;
        }

        // Insert the new user into the database
        String query = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            stmt.setString(3, role);  // Role can be 'manager' or 'employee'
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

    // Method to check if a username is already taken
    private static boolean isUsernameTaken(String username) {
        String query = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            var rs = stmt.executeQuery();

            return rs.next(); // If a row exists, the username is taken
        } catch (SQLException e) {
            System.out.println("Error checking username availability: " + e.getMessage());
        }
        return false;
    }


    // Method to authenticate user and return their role if successful
    public static String getRoleIfAuthenticated(String username, String password) {
        String query = "SELECT password_hash, role FROM users WHERE username = ?";            try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPasswordHash = rs.getString("password_hash");
                String role = rs.getString("role");
    
                if (BCrypt.checkpw(password, storedPasswordHash)) {
                    return role; // Return role if authentication is successful
                }
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
        }
        return null; // Return null if authentication fails
    }
}
  
