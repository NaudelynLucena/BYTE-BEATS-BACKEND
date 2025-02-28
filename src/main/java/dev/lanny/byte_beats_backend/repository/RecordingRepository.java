package dev.lanny.byte_beats_backend.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
        List<RecordingDto> recording = new ArrayList<>();
        String query = "SELECT * FROM recordings";
        try (Statement stmt = connection.createStatement();

                ResultSet rc = stmt.executeQuery(query)) {
            while (rc.next()) {
                recording.add(new RecordingDto(
                        rc.getInt("id"),
                        rc.getString("title"),
                        rc.getDouble("duration")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recording;
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
