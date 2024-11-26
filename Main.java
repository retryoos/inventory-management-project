package app;

import app.users.Manager;
import app.users.Employee;
import app.services.AuthService;
import java.services.DatabaseConnection;

// Compile and Run
// javac app/Main.java app/functionalities/*.java app/users/*.java
// java app.Main

// Currently just test file

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome! Please choose an option:");
        System.out.println("1. Login");
        System.out.println("2. Register");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        if (choice == 1) {
            login();
        } else if (choice == 2) {
            // Handle registration (simplified)
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Role (manager/employee): ");
            String role = scanner.nextLine().toLowerCase(); // Handle role input case-insensitively
        
            // Call AuthService to register the user
            boolean registrationSuccess = AuthService.registerUser(null, username, password, role);
            if (registrationSuccess) {
                System.out.println("User registered successfully!");
                
                // After registration, call the login method to authenticate the new user
                login();
            } else {
                System.out.println("Registration failed. Please try again.");
            }
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    // Login
    private static void login() {
        // Handle login
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
    
        // Attempt to authenticate and create a user
        AuthService.UserDetails userDetails = AuthService.authenticateUser(username, password);
    
        if (userDetails != null) {
            // If authentication is successful, proceed
            System.out.println("Welcome, user ID: " + userDetails.id + ", Role: " + userDetails.role);
            
            // Create a database connection after successful login
            Connection conn = DatabaseConnection.connect();
    
            if ("manager".equals(userDetails.role)) {
                // Manager-specific logic
                Manager manager = new Manager(username, password, userDetails.id, conn);
                System.out.println("Manager logged in successfully!");
                showManagerMenu(manager); // Call the manager-specific menu
            } else if ("employee".equals(userDetails.role)) {
                // Employee-specific logic
                Employee employee = new Employee(username, password, userDetails.id, conn);
                System.out.println("Employee logged in successfully!");
                showEmployeeMenu(employee); // Call the employee-specific menu
            } else {
                System.out.println("Invalid role. Please try again.");
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    // Manager Menu
    private static void showManagerMenu(Manager manager) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nManager Menu:");
        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. Update Product");
        System.out.println("4. View All Products");
        System.out.println("5. Generate Sales Report");
        System.out.println("6. Logout");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                // Implement add product logic
                break;
            case 6:
                System.out.println("Logging out...");
                manager.closeConnection();  // Close the database connection
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    // Employee Menu
    private static void showEmployeeMenu(Employee employee) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEmployee Menu:");
        System.out.println("1. View Products");
        System.out.println("2. Logout");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                // Implement view products logic
                break;
            case 2:
                System.out.println("Logging out...");
                employee.closeConnection();  // Close the database connection
                break;
            default:
                System.out.println("Invalid choice");
        }
    }
}