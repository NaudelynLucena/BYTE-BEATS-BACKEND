package dev.lanny.byte_beats_backend.repository;

import dev.lanny.byte_beats_backend.dtos.RecordingDto;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecordingRepository {
    private static final Logger LOGGER = Logger.getLogger(RecordingRepository.class.getName());

    public RecordingRepository() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS recordings (" +
                    "id INT PRIMARY KEY, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "duration DOUBLE NOT NULL)";
        try (Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            LOGGER.info("Tabla 'recordings' verificada/cargada correctamente.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar la base de datos", e);
        }
    }

    public void saveRecording(RecordingDto recording) {
        String sql = "INSERT INTO recordings (id, title, duration) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, recording.id());
            stmt.setString(2, recording.title());
            stmt.setDouble(3, recording.duration());
            stmt.executeUpdate();
            LOGGER.info("Grabación guardada correctamente: " + recording);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar la grabación", e);
        }
    }

    public RecordingDto getRecordingById(int id) {
        String sql = "SELECT * FROM recordings WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new RecordingDto(rs.getInt("id"), rs.getString("title"), rs.getDouble("duration"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener grabación con ID: " + id, e);
        }
        return null;
    }

    public List<RecordingDto> getAllRecordings() {
        List<RecordingDto> recordings = new ArrayList<>();
        String sql = "SELECT * FROM recordings";
        try (Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recordings.add(new RecordingDto(rs.getInt("id"), rs.getString("title"), rs.getDouble("duration")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todas las grabaciones", e);
        }
        return recordings;
    }

    public boolean updateRecording(int id, RecordingDto recording) {
        String sql = "UPDATE recordings SET title = ?, duration = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recording.title());
            stmt.setDouble(2, recording.duration());
            stmt.setInt(3, id);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar grabación con ID: " + id, e);
            return false;
        }
    }

    public boolean destroyRecording(int id) {
        String sql = "DELETE FROM recordings WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar grabación con ID: " + id, e);
            return false;
        }
    }
}
