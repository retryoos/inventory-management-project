import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DATABASE_URL = "jdbc:postgresql://c3gtj1dt5vh48j.cluster-czrs8kj4isg7.us-east-1.rds.amazonaws.com:5432/d84ajq8njeuadc";
    private static final String USERNAME = "uai2q0u435jbpp";
    private static final String PASSWORD = "pb8b5cd413630888544544fbb48e7af274ddc2a5190c79ec52b80f058bcdeed9d";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
    }
}
