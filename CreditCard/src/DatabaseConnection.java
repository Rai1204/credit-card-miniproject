import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/creditcarddb";
    private static final String USER = "root";  // Change if using a different MySQL user
    private static final String PASSWORD = "root";  // Change if using a different MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
