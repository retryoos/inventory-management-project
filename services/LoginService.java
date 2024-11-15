package app.services;

import app.users.User;

import java.util.HashMap;
import java.util.Map;

public class LoginService {
    private Map<String, User> userDB = new HashMap<>();

    // Add users to DB
    public void registerUser(User user) {
        userDB.put(user.getUsername(), user);
    }

    public User login(String username, String password) {
        User user = userDB.get(username);

        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful - ["+ user.getRole() +"]");
            return user;
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }
}