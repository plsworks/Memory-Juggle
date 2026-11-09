package memoryjuggle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL  = "jdbc:mysql://localhost:3306/memory_juggle";
    private static final String USER = "root";
    private static final String PASS = "";

   public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASS);
}

public static void main(String[] args) {
    try (Connection conn = getConnection()) {
        System.out.println("Connected");
    } catch (SQLException e) {
        System.out.println("Failed to connect: " + e.getMessage());
    }
}
}