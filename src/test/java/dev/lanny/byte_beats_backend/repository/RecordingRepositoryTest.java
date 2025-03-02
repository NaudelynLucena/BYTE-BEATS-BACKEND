package dev.lanny.byte_beats_backend.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import dev.lanny.byte_beats_backend.dtos.NoteDto;
import dev.lanny.byte_beats_backend.dtos.RecordingDto;
import dev.lanny.byte_beats_backend.persistence.DatabaseConnection;

public class RecordingRepositoryTest {

    private static final String TEST_INSTRUMENT = "test_instrument";
    private static final long TEST_DURATION = 30000L;
    private static final String TEST_TIMESTAMP = LocalDateTime.now().toString();
    private static final List<NoteDto> TEST_NOTES = new ArrayList<>();

    @BeforeAll
    static void setup() {
        System.setProperty("db.url", "jdbc:h2:file:./data/musicaDB");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
    }

    @BeforeEach
    void resetDatabase() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM notes");
            stmt.execute("DELETE FROM recordings");
        }
    }

    @Test
    void getRecordingById_NonExistentRecording_ReturnsNull() {

        RecordingRepository repo = new RecordingRepository();

        RecordingDto recording = repo.getRecordingById(1);

        assertNull(recording);
    }

    @Test
    void updateRecording_NonExistentRecording_ReturnsFalse() {

        RecordingRepository repo = new RecordingRepository();
        RecordingDto recording = new RecordingDto(
                1,
                TEST_TIMESTAMP,
                TEST_DURATION,
                TEST_INSTRUMENT,
                TEST_NOTES);

        boolean success = repo.updateRecording(1, recording);

        assertFalse(success);
    }

    @Test
    void destroyRecording_NonExistentRecording_ReturnsFalse() {

        RecordingRepository repo = new RecordingRepository();

        boolean success = repo.destroyRecording(1);

        assertFalse(success);
    }
}
