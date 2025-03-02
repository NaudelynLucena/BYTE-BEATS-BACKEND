package dev.lanny.byte_beats_backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                recordings.add(new RecordingDto(
                        rs.getInt("id"),
                        rs.getString("timestamp"),
                        rs.getLong("duration"),
                        rs.getString("instrument"),
                        getNotesByRecordingId(rs.getInt("id"))));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener grabaciones: " + e.getMessage());
        }
        return recordings;
    }

    public RecordingDto getRecordingById(int recordingId) {
        String query = "SELECT * FROM recordings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, recordingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new RecordingDto(
                        rs.getInt("id"),
                        rs.getString("timestamp"),
                        rs.getLong("duration"),
                        rs.getString("instrument"),
                        getNotesByRecordingId(recordingId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveRecording(RecordingDto recordingDto) {
        String query = "INSERT INTO recordings (timestamp, duration, instrument) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, recordingDto.timestamp());
            stmt.setLong(2, recordingDto.duration());
            stmt.setString(3, recordingDto.instrument());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                saveNotesForRecording(generatedId, recordingDto.notes());
            }
            System.out.println("Grabación guardada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al guardar grabación: " + e.getMessage());
        }
    }

    public boolean updateRecording(int recordingId, RecordingDto updatedRecording) {
        String query = "UPDATE recordings SET timestamp = ?, duration = ?, instrument = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, updatedRecording.timestamp());
            stmt.setLong(2, updatedRecording.duration());
            stmt.setString(3, updatedRecording.instrument());
            stmt.setInt(4, recordingId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean destroyRecording(int recordingId) {
        String query = "DELETE FROM recordings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, recordingId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveNotesForRecording(int recordingId, List<NoteDto> notes) {
        String query = "INSERT INTO notes (recordingId, midi, startTime, stopTime, duration) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (NoteDto note : notes) {
                stmt.setInt(1, recordingId);
                stmt.setInt(2, note.midi());
                stmt.setLong(3, note.startTime());
                stmt.setLong(4, note.stopTime());
                stmt.setLong(5, note.duration());
                stmt.addBatch();
            }
            stmt.executeBatch();
            System.out.println("Notas guardadas para la grabación.");
        } catch (SQLException e) {
            System.err.println("Error al guardar notas: " + e.getMessage());
        }
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
            System.err.println(" Error al obtener notas: " + e.getMessage());
        }
        return notes;
    }

}
