package dev.lanny.byte_beats_backend.persistence;

import java.sql.*;




public class H2Database {
    // private static final String DB_URL =
    // "jdbc:h2:mem:byte_beats;DB_CLOSE_DELAY=-1"; //defino la conexion a la base de
    // datos indicando que estara en memoria y mantiene la la base de datos activa
    // mientras se ejecuta la aplicacion
    private String DB_URL;// url confiable para la base de datos
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS instruments (" +
            "id INT PRIMARY KEY, " +
            "name VARCHAR(255) NOT NULL, " +
            "type VARCHAR(255) NOT NULL, " +
            "sound VARCHAR(255) NOT NULL)";

    // CONSTRUCTOR POR DEFECTO: USA LA BASE DE DATOS EN MEMORIA
    public H2Database() {
        this("jdbc:h2:mem:byte_beats;DB_CLOSE_DELAY=-1");
    }

    // constructor con parametros que em permita personalizar la base de datos pàra
    // las pruebas
    public H2Database(String dbUrl) {
        this.DB_URL = dbUrl;
        initDatabase();
    }

    public void initDatabase() {
        try {
            // Cargar el driver de H2 manualmente para evitar errores de conexión
            Class.forName("org.h2.Driver");

            try (Connection connection = DriverManager.getConnection(DB_URL);
                    Statement statement = connection.createStatement()) {

                System.out.println("Creando tabla si no existe...");
                statement.execute(CREATE_TABLE_SQL);
                System.out.println("Tabla 'instruments' creada o ya existente.");

            }
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: No se encontró el driver de H2.");
            throw new RuntimeException("Error al cargar el driver H2", e);
        } catch (SQLException e) {
            System.out.println("ERROR al crear la tabla: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al conectar con la base de datos H2", e);
        }
    }

   

    // Devuelve una conexión a la base de datos.

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

}
