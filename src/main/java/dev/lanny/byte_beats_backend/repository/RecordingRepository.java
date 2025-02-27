package dev.lanny.byte_beats_backend.repository;

import dev.lanny.byte_beats_backend.dtos.RecordingDto;
import utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordingRepository {

    // Aquí usamos DatabaseManager para obtener la conexión.
    private final Connection connection;

    public RecordingRepository() {
        // Usamos DatabaseManager para obtener la conexión
        this.connection = DatabaseManager.getConnection();
    }

    public List<RecordingDto> getAllRecordings() {
        List<RecordingDto> recordings = new ArrayList<>();
        String query = "SELECT * FROM recordings";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                recordings.add(new RecordingDto(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("duration")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordings;
    }

    public RecordingDto getRecordingById(int id) {
        String query = "SELECT * FROM recordings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new RecordingDto(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("duration"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveRecording(RecordingDto recordingDto) {
        String query = "INSERT INTO recordings (id, title, duration) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, recordingDto.id());
            stmt.setString(2, recordingDto.title());
            stmt.setDouble(3, recordingDto.duration());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateRecording(int id, RecordingDto recordingDto) {
        String query = "UPDATE recordings SET title = ?, duration = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, recordingDto.title());
            stmt.setDouble(2, recordingDto.duration());
            stmt.setInt(3, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean destroyRecording(int id) {
        String query = "DELETE FROM recordings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
