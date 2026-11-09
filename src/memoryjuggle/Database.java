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
static {
    try (Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/", USER, PASS);
         java.sql.Statement st = c.createStatement()) {
        st.executeUpdate("CREATE DATABASE IF NOT EXISTS memory_juggle");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS memory_juggle.card_sets ("
            + "set_id INT AUTO_INCREMENT PRIMARY KEY, set_name VARCHAR(100) NOT NULL UNIQUE)");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS memory_juggle.card_pairs ("
            + "pair_id INT AUTO_INCREMENT PRIMARY KEY, set_id INT NOT NULL, "
            + "word VARCHAR(255) NOT NULL, match_word VARCHAR(255) NOT NULL, "
            + "FOREIGN KEY (set_id) REFERENCES memory_juggle.card_sets(set_id) ON DELETE CASCADE)");
    } catch (SQLException e) {
        System.err.println("Could not set up database: " + e.getMessage());
    }
}
public static void main(String[] args) {
    try (Connection conn = getConnection()) {
        System.out.println("Connected");
    } catch (SQLException e) {
        System.out.println("Failed to connect: " + e.getMessage());
    }
}
}