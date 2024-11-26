package app.functionalities;

import java.io.IOError;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Sales {

    public void generateReport(Connection conn, int employeeId, String timeFrame) {
        if ("max".equals(timeFrame)) {
            String query = "SELECT * FROM sales WHERE employee_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
                try (FileWriter writer = new FileWriter("sales_report_" + employeeId + ".csv")) {
                    writer.append("Sale ID, Product Name, Quantity, Price, Date\n");
                    while (rs.next()) {
                        writer.append(rs.getInt("id") + ",");
                        writer.append(rs.getString("product_name") + ",");
                        writer.append(rs.getInt("quantity_sold") + ",");
                        writer.append(rs.getDouble("price") + ",");
                        writer.append(rs.getDouble("total_amount") + ",");
                        writer.append(rs.getDate("date") + "\n");
                    }
                    System.out.println("Sales report generated: sales_report_" + employeeId + ".csv");
                } catch (SQLException | IOException e) {
                    System.out.println("Error generating sales report: " + e.getMessage());
                }
            }
<<<<<<< HEAD
        } else if (timeFrame) {
            // think for how to format this search on month specific else stick with just doing an employeeId sales search
        } else {
            // exception handling (maybe not needed)
        }
=======
        } 
>>>>>>> bbd5e6c (Update all files with latest changes)
    }
}