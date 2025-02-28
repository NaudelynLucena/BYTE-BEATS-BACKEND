package dev.lanny.byte_beats_backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dev.lanny.byte_beats_backend.dtos.NoteDto;
import dev.lanny.byte_beats_backend.dtos.RecordingDto;
import dev.lanny.byte_beats_backend.persistence.DatabaseConnection;

public class RecordingRepository {
    private final Connection connection;

    public RecordingRepository() {
        try {
            this.connection = DatabaseConnection.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("error al conectarse con la base de datos");

        }
    }

    public List<RecordingDto> getAllRecordings() {
        List<RecordingDto> recordings = new ArrayList<>();
        String query = "SELECT * FROM recordings";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                recordings.add(new RecordingDto(
                        rs.getInt("id"),
                        rs.getString("timestamp"),
                        rs.getLong("duration"),
                        rs.getString("instrument"),
                        getNotesByRecordingId(rs.getInt("id"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordings;
    }

    public RecordingDto getRecordingById(int id) {

        String query = "SELECT * FROM recordings WHERE id =? ";
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
    private List<NoteDto> getNotesByRecordingId(int recordingId) {
        List<NoteDto> notes = new ArrayList<>();
        String query = "SELECT midi, startTime, stopTime, duration FROM notes WHERE recordingId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, recordingId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notes.add(new NoteDto(
                        rs.getInt("midi"),
                        rs.getLong("startTime"),
                        rs.getLong("stopTime"),
                        rs.getLong("duration")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }
}
