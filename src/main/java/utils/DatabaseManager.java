package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static final String URL = "jdbc:h2:~/test"; // Ruta de la BD H2 (puedes cambiarla)
    private static final String USER = "sa"; // Usuario por defecto en H2
    private static final String PASSWORD = ""; // Sin contraseña por defecto
    private static Connection connection = null;

    // Constructor privado para evitar instanciación externa
    private DatabaseManager() {}

    // Método estático para obtener la conexión a la base de datos
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                LOGGER.info("Conexión establecida con la base de datos H2.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al conectar con la base de datos", e);
            }
        }
        return connection;
    }

    // Método para cerrar la conexión cuando la aplicación termine
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Conexión cerrada correctamente.");
                connection = null;
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión", e);
            }
        }
    }
}
