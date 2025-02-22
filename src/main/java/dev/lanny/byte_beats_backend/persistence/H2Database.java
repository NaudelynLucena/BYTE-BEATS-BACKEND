package dev.lanny.byte_beats_backend.persistence;

import java.sql.*;
import java.util.*;

import dev.lanny.byte_beats_backend.models.Instruments;

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

    public void saveInstruments(Instruments instruments) {
        if (instruments == null ||
                instruments.getname() == null || instruments.getname().trim().isEmpty() ||
                instruments.getType() == null || instruments.getType().trim().isEmpty() ||
                instruments.getSound() == null || instruments.getSound().trim().isEmpty()) {

            throw new IllegalArgumentException("❌ No se puede insertar un instrumento con valores nulos o vacíos.");
        }

        String sql = "INSERT INTO instruments (id, name, type, sound) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL);
                PreparedStatement pStatement = connection.prepareStatement(sql)) {

            pStatement.setInt(1, instruments.getId());
            pStatement.setString(2, instruments.getname());
            pStatement.setString(3, instruments.getType());
            pStatement.setString(4, instruments.getSound());

            int affectedRows = pStatement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("⚠️ No se insertó ningún registro en la base de datos.");
            } else {
                System.out.println("✅ Instrumento guardado correctamente: " + instruments);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el instrumento en la base de datos.", e);
        }
    }

    public List<Instruments> loadInstruments() {
        List<Instruments> instruments = new ArrayList<>();
        String sql = "SELECT * FROM instruments";

        try (Connection connection = DriverManager.getConnection(DB_URL);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Cargando instrumentos desde la base de datos...");
            while (resultSet.next()) {
                Instruments instrument = new Instruments(
                        resultSet.getString("name"),
                        resultSet.getInt("id"),
                        resultSet.getString("type"),
                        resultSet.getString("sound"));
                instruments.add(instrument);
                System.out.println("Instrumento encontrado después de consulta: " + instrument);
            }

        } catch (SQLException e) {
            System.out.println("ERROR al recuperar los instrumentos: " + e.getMessage());
            e.printStackTrace();
        }

        return instruments;
    }

    public boolean deleteInstruments(int id) {
        String sql = "DELETE FROM instruments WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println("🔍 Intentando eliminar el instrumento con ID: " + id);

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            System.out.println("Filas afectadas después de eliminar: " + affectedRows);

            return affectedRows > 0; // Devuelve true si se eliminó un registro
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateInstruments(int id, String newName, String newType, String newSoundFile) {
        String sql = "UPDATE instruments SET name = ?, type = ?, sound = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newType);
            pstmt.setString(3, newSoundFile);
            pstmt.setInt(4, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Devuelve true si se actualizó algún registro
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
