package app;

import app.users.Manager;
import app.users.Employee;
import app.functionalities.StockUpdate;
import app.services.LoginService;

// Compile and Run
// javac app/Main.java app/functionalities/*.java app/users/*.java
// java app.Main

// Currently just test file

public class Main {
    public static void main(String[] args) {
        // Create stock manager
        StockUpdate stockUpdate = new StockUpdate();

        // Create login service
        LoginService loginService = new LoginService();

        // Register users
        Manager manager = new Manager("admin", "admin123", "M001");
        Employee employee = new Employee("user", "user123", "E001");

        loginService.registerUser(manager);
        loginService.registerUser(employee);

        // Authenticate and test functionality
        Users loggedInUser = loginService.login("admin", "admin123");
        if (loggedInUser instanceof Manager) {
            Manager loggedInManager = (Manager) loggedInUser;
            loggedInManager.addProduct("Laptop", 10, 1500.0, stockUpdate);
        }

        loggedInUser = loginService.login("user", "user123");
        if (loggedInUser instanceof Employee) {
            Employee loggedInEmployee = (Employee) loggedInUser;
            loggedInEmployee.sellProduct("Laptop", 2, stockUpdate);
        }
    }
}
