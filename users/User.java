package app.users;

public abstract class User {
    private String username;
    private String password;

    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }   

    // Access username and pw
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}