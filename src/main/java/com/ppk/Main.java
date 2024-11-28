package com.ppk;

import com.ppk.users.Manager;
import com.ppk.users.Employee;

import com.ppk.functionalities.Inventory;
import com.ppk.functionalities.Sales;
import com.ppk.functionalities.EmployeeManage;

import com.ppk.services.AuthService;
import com.ppk.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner; 

// mvn compile -e
// mvn exec:java -e

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
                System.out.println("Welcome, enter your credentials to login.");
                // After registration, call the login method to authenticate the new user
                login();
            } else {
                System.out.println("Registration failed. Please try again.");
            }
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
        scanner.close();
    }

    private static void login() {
        // Handle login
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
    
        // Attempt to authenticate and create a user
        AuthService.UserDetails userDetails = AuthService.authenticateUser(username, password);
    
        if (userDetails != null) {
            // If authentication is successful, proceed
            System.out.println("Welcome, user ID: " + userDetails.id + ", Role: " + userDetails.role);
            
            // Declare conn outside the try block to use it later
            Connection conn = null;
            
            // Create a database connection after successful login
            try {
                conn = DatabaseConnection.connect();
            } catch (SQLException e) {
                System.out.println("Error connecting to the database: " + e.getMessage());
            }
    
            if (conn != null) {
                if ("manager".equals(userDetails.role)) {
                    // Manager-specific logic
                    Manager manager = new Manager(username, password, userDetails.id, conn);
                    System.out.println("Manager logged in successfully!");
                    showManagerMenu(manager); // Call the manager-specific menu
                } else if ("employee".equals(userDetails.role)) {
                    // Employee-specific logic
                    Employee employee = new Employee(username, password, userDetails.id, conn);  // Pass the connection
                    System.out.println("Employee logged in successfully!");
                    showEmployeeMenu(employee); // Call the employee-specific menu
                }
            } else {
                System.out.println("Unable to establish a database connection.");
            }
        } else {
            login();
        }
        scanner.close();
    }
    

    // Manager Menu
    private static void showManagerMenu(Manager manager) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nManager Menu:");
        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. Update Product");
        System.out.println("4. Export Inventory Report");
        System.out.println("5. Search for Specific Item");
        System.out.println("6. Generate All Sales Report");
        System.out.println("7. Generate Employee Specific Report");
        System.out.println("8. Add Sale to Employee");
        System.out.println("9. Cancel Sale");
        System.out.println("10. Remove User");
        System.out.println("11. View All Employees Report");
        System.out.println("12. Logout");

        // Initiate variables
        int productId;
        String productName;
        double price;
        int qty;
        int employee_id_sale;
        int saleId;
        int employee_id_remove;

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter product name: ");
                productName = scanner.nextLine();
                
                // Collect quantity
                System.out.print("Enter quantity: ");
                while (true) { // Validate input
                    try {
                        qty = Integer.parseInt(scanner.nextLine());
                        if (qty > 0) break;
                        else System.out.print("Quantity must be a positive integer. Try again: ");
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid input. Please enter a positive integer for quantity: ");
                    }
                }
                
                // Collect price
                System.out.print("Enter price: ");
                while (true) { // Validate input
                    try {
                        price = Double.parseDouble(scanner.nextLine());
                        if (price > 0) break;
                        else System.out.print("Price must be a positive number. Try again: ");
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid input. Please enter a positive number for price: ");
                    }
                }
                manager.addProduct(productName, qty, price, new Inventory());
                showManagerMenu(manager);
            case 2:
                System.out.print("Enter product ID to remove: ");
            
                // Validate the input
                while (true) {
                    try {
                        productId = Integer.parseInt(scanner.nextLine()); // Use nextLine() for better error handling
                        if (productId > 0) { // Check for positive IDs
                            break;
                        } else {
                            System.out.print("Product ID must be a positive integer. Try again: ");
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid input. Please enter a valid integer for Product ID: ");
                    }
                }
            
                // Call the removeProduct method
                manager.removeProduct(productId, new Inventory());
                showManagerMenu(manager);
            case 3:
                System.out.print("Enter Product ID to update: ");
        
                // Validate the input for Product ID
                while (true) {
                    try {
                        productId = Integer.parseInt(scanner.nextLine());
                        if (productId > 0) {
                            break;
                        } else {
                            System.out.print("Product ID must be a positive integer. Try again: ");
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid input. Please enter a valid integer for product ID: ");
                    }
               }
        
                // Get new product name from user
                System.out.print("Enter new product name: ");
                productName = scanner.nextLine();
        
                // Get new product quantity from user
                while (true) {
                    System.out.print("Enter new quantity: ");
                    try {
                        qty = Integer.parseInt(scanner.nextLine());
                        if (qty >= 0) {  // Ensure quantity is non-negative
                            break;
                        } else {
                            System.out.print("Quantity must be non-negative. Try again: ");
                        }
                    } catch (NumberFormatException e) {
                     System.out.print("Invalid input. Please enter a valid integer for quantity: ");
                    }
                }
        
                // Get new price from user
                while (true) {
                    System.out.print("Enter new price: ");
                    try {
                        price = Double.parseDouble(scanner.nextLine());
                        if (price > 0) {  // Ensure price is positive
                            break;
                        } else {
                            System.out.print("Price must be positive. Try again: ");
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid input. Please enter a valid price: ");
                    }
                }
        
                // Call the update method in Inventory or Manager
                manager.updateProduct(productId, productName, qty, price, new Inventory());
                showManagerMenu(manager);
            case 4:
                manager.viewAllProducts(new Inventory());
                showManagerMenu(manager);
            case 5:
                System.out.print("Enter product name to search: ");
                productName = scanner.nextLine();
                manager.searchProduct(productName, new Inventory());
                showManagerMenu(manager);
            case 6:
                manager.generateAllSalesReport(new Sales());
                showManagerMenu(manager);
            case 7:
                System.out.println("Enter employee ID to generate sales report: ");
                int employee_id_report = scanner.nextInt();
                manager.generateEmployeesSalesReport(employee_id_report, new Sales());
                showManagerMenu(manager);
            case 8:
                System.out.print("Enter product name: ");
                productName = scanner.nextLine();
                
                // Collect and validate quantity input
                while (true) {
                    System.out.print("Enter quantity: ");
                    if (scanner.hasNextInt()) {
                        qty = scanner.nextInt();
                        if (qty > 0) {
                            break; // Valid quantity input, exit loop
                        } else {
                            System.out.println("Quantity must be a positive integer.");
                        }
                    } else {
                        System.out.println("Invalid input! Please enter a valid integer for quantity.");
                        scanner.next(); // Consume the invalid input
                    }
                }
            
                // Collect and validate price input
                while (true) {
                    System.out.print("Enter price: ");
                    if (scanner.hasNextDouble()) {
                        price = scanner.nextDouble();
                        if (price > 0) {
                            break; // Valid price input, exit loop
                        } else {
                            System.out.println("Price must be greater than 0.");
                        }
                    } else {
                        System.out.println("Invalid input! Please enter a valid number for price.");
                        scanner.next(); // Consume the invalid input
                    }
                }
                
                // Collect and validate productId input
                while (true) {
                    System.out.print("Enter product id: ");
                    if (scanner.hasNextInt()) {
                        productId = scanner.nextInt();
                        if (productId > 0) {
                            break; // Valid quantity input, exit loop
                        } else {
                            System.out.println("Product id must be a positive integer.");
                        }
                    } else {
                        System.out.println("Invalid input! Please enter a valid integer for product id.");
                        scanner.next(); // Consume the invalid input
                    }
                }
                
                // Collect and validate employee id input
                while (true) {
                    System.out.print("Enter employee id: ");
                    if (scanner.hasNextInt()) {
                        employee_id_sale = scanner.nextInt();
                        if (employee_id_sale > 0) {
                            break; // Valid quantity input, exit loop
                        } else {
                            System.out.println("Employee id must be a positive integer.");
                        }
                    } else {
                        System.out.println("Invalid input! Please enter a valid integer for employee id.");
                        scanner.next(); // Consume the invalid input
                    }
                }

                // Call the addSale method
                manager.addSale(productName, qty, price, employee_id_sale, productId, new Sales());
                // Show the manager menu again after the action is done
                showManagerMenu(manager);
            case 9:
                System.out.print("Enter sale ID to cancel: ");

                while (true) {
                    try {
                        saleId = Integer.parseInt(scanner.nextLine());
                        if (saleId > 0) {
                            break;
                        } else {
                            System.out.print("Sale ID must be a positive integer. Try again: ");
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid input. Please enter a valid integer for Sale ID: ");
                    }
                }
                manager.cancelSale(saleId, new Sales());
                showManagerMenu(manager);
            case 10:
                System.out.print("Enter employee ID to remove: ");

                while (true) {
                    try {
                        employee_id_remove = Integer.parseInt(scanner.nextLine());
                        if (employee_id_remove > 0) {
                            break;
                        } else {
                            System.out.print("Employee ID must be a positive integer. Try again: ");
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid input. Please enter a valid integer for Employee ID: ");
                    }
                }

                manager.removeEmployee(employee_id_remove, new EmployeeManage());
                showManagerMenu(manager);
            case 11:
                manager.generateAllEmployeesReport(new EmployeeManage());
                showManagerMenu(manager);
            case 12:
                System.out.println("Logging out...");
                manager.closeConnection();  // Close the database connection
                break;
            default:
                System.out.println("Invalid choice");
                showManagerMenu(manager);
        }
        scanner.close();
    }

    // Employee Menu
    private static void showEmployeeMenu(Employee employee) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEmployee Menu:");
        System.out.println("1. Export Inventory Report");
        System.out.println("2. Search Specific Item");
        System.out.println("3. Add Sale");
        System.out.println("4. Generate Sales Report");
        System.out.println("5. Logout");

        String productName;
        double price;
        int qty;
        int productId;

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                employee.viewInventory(new Inventory());
                showEmployeeMenu(employee);
            case 2:
                System.out.print("Enter product name to search: ");
                productName = scanner.nextLine();
                employee.searchItem(productName, new Inventory());
                showEmployeeMenu(employee);
            case 3:
                System.out.print("Enter product name: ");
                productName = scanner.nextLine();
                
                // Collect and validate quantity input
                while (true) {
                    System.out.print("Enter quantity: ");
                    if (scanner.hasNextInt()) {
                        qty = scanner.nextInt();
                        if (qty > 0) {
                            break; // Valid quantity input, exit loop
                        } else {
                            System.out.println("Quantity must be a positive integer.");
                        }
                    } else {
                        System.out.println("Invalid input! Please enter a valid integer for quantity.");
                        scanner.next(); // Consume the invalid input
                    }
                }
            
                // Collect and validate price input
                while (true) {
                    System.out.print("Enter price per unit: ");
                    if (scanner.hasNextDouble()) {
                        price = scanner.nextDouble();
                        if (price > 0) {
                            break; // Valid price input, exit loop
                        } else {
                            System.out.println("Price must be greater than 0.");
                        }
                    } else {
                        System.out.println("Invalid input! Please enter a valid number for price.");
                        scanner.next(); // Consume the invalid input
                    }
                }

                // Collect and validate productId input
                while (true) {
                    System.out.print("Enter quantity: ");
                    if (scanner.hasNextInt()) {
                        productId = scanner.nextInt();
                        if (productId > 0) {
                            break; // Valid quantity input, exit loop
                        } else {
                            System.out.println("Product ID must be a positive integer.");
                        }
                    } else {
                        System.out.println("Invalid input! Please enter a valid integer for Product ID.");
                        scanner.next(); // Consume the invalid input
                    }
                }
            
                // Call the addSale method
                employee.addSale(productName, qty, price, employee.getEmployeeId(), productId, new Sales());
                
                // Show the employee menu again after the action is done
                showEmployeeMenu(employee);
            case 4:
                employee.generateReport(employee.getEmployeeId(), new Sales());
                showEmployeeMenu(employee);
            case 5:
                System.out.println("Logging out...");
                employee.closeConnection();  // Close the database connection
                break;
            default:
                System.out.println("Invalid choice");
                showEmployeeMenu(employee);
        }
        scanner.close();
    }
}