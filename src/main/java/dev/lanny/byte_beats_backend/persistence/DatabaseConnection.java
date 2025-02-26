package dev.lanny.byte_beats_backend.persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dev.lanny.byte_beats_backend.config.ConfigLoader;

public class DatabaseConnection {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = createConnection();
        }
        return connection;
    }

    private static Connection createConnection() throws SQLException {
        String url = ConfigLoader.get("db.url");
        String user = ConfigLoader.get("db.user");
        String password = ConfigLoader.get("db.password");

        return DriverManager.getConnection(url, user, password);
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
