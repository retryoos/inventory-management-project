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
    private static String currentUser = null; // Username of the logged-in user
    private static String currentRole = null; // Role of the logged-in user

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Register User (Manager Only)");
            System.out.println("3. Logout");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    registerUser(scanner);
                    break;
                case 3:
                    logout();
                    break;
                case 4:
                    System.out.println("Exiting application.");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void login(Scanner scanner) {
        System.out.println("Login");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Authenticate user
        String role = AuthService.getRoleIfAuthenticated(username, password);
        if (role != null) {
            currentUser = username;
            currentRole = role;
            System.out.println("Login successful. Welcome, " + role + "!");
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void registerUser(Scanner scanner) {
        if (!"manager".equals(currentRole)) {
            System.out.println("Only managers can register users.");
            return;
        }

        System.out.println("Register a new user:");
        System.out.print("New Username: ");
        String newUsername = scanner.nextLine();
        System.out.print("New Password: ");
        String newPassword = scanner.nextLine();
        System.out.print("Role (manager/employee): ");
        String role = scanner.nextLine();

        boolean success = AuthService.registerUser(newUsername, newPassword, role, currentRole);
        if (success) {
            System.out.println("User registered successfully.");
        } else {
            System.out.println("Failed to register user.");
        }
    }

    private static void logout() {
        if (currentUser == null) {
            System.out.println("No user is logged in.");
            return;
        }
        System.out.println("Logging out " + currentUser);
        currentUser = null;
        currentRole = null;
    }
}
