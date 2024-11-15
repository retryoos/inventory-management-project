package app.users;

import app.fuctionalities.StockUpdate;

// maybe manager from sales can conduct a report to download (excel) to pull from db table sales

public class Manager extends User {
    private int id;

    // Constructor
    public Manager(String username, String password, int id) {
        super(username, password);
        this.id = id;
    }

    // Accessor method for id
    public int getId() {
        return id;
    }

    @Override
    public String getRole() {
        return "Admin/Manager";
    }

    public void addProduct(String productName, double price, int qty, StockUpdate stockUpdate) {
        stockUpdate.add(productName, price, qty);
    }

    public void removeProduct(String productName, StockUpdate stockUpdate) {
        stockUpdate.remove(productName);
    }

    public void updateProduct(String productName, double price, int qty, StockUpdate stockUpdate) {
        stockUpdate.update(productName, price, qty);
    }
}