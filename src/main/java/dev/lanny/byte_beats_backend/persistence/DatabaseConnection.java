package dev.lanny.byte_beats_backend.persistence;
import java.sql.Statement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dev.lanny.byte_beats_backend.config.ConfigLoader;

public class DatabaseConnection {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = createConnection();
            executeSqlScript("src/main/resources/data.sql"); 
        }
        return connection;
    }

    private static Connection createConnection() throws SQLException {
        String url = ConfigLoader.get("db.url");
        String user = ConfigLoader.get("db.user");
        String password = ConfigLoader.get("db.password");

        return DriverManager.getConnection(url, user, password);
    }
    private static void executeSqlScript(String filePath) {
        try {
            String sql = new String(Files.readAllBytes(Paths.get(filePath)));
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sql);
                System.out.println("Script SQL ejecutado correctamente.");
            }
        } catch (IOException | SQLException e) {
            System.err.println("Error al ejecutar el script SQL: " + e.getMessage());
        }
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
