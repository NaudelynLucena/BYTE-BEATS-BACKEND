package dev.lanny.byte_beats_backend.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RecordingDtoTest {

    @Test
    void createRecordingDto_CorrectFields_RecordingCreatedSuccessfully() {

        int recordingId = 1;
        String timestamp = LocalDateTime.now().toString();
        long duration = 480;
        String instrument = "Piano";
        List<NoteDto> notes = new ArrayList<>();
        notes.add(new NoteDto(60, 0, 240, 240));

        RecordingDto recording = new RecordingDto(recordingId, timestamp, duration, instrument, notes);

        assertEquals(recordingId, recording.id());
        assertEquals(timestamp, recording.timestamp());
        assertEquals(duration, recording.duration());
        assertEquals(instrument, recording.instrument());
        assertEquals(notes, recording.notes());
    }

    @Test
    void compareRecordings_SameValues_AreEqual() {
        List<NoteDto> notes = new ArrayList<>();
        notes.add(new NoteDto(60, 0, 240, 240));

        RecordingDto recording1 = new RecordingDto(
                1,
                "2025-03-02T15:30:00",
                480,
                "Piano",
                notes);

        RecordingDto recording2 = new RecordingDto(
                1,
                "2025-03-02T15:30:00",
                480,
                "Piano",
                new ArrayList<>(notes));

        assertTrue(recording1.equals(recording2));
        assertEquals(recording1.hashCode(), recording2.hashCode());
    }

    @Test
    void compareRecordings_DifferentValues_NotEqual() {
        List<NoteDto> notes = new ArrayList<>();
        notes.add(new NoteDto(60, 0, 240, 240));

        RecordingDto recording1 = new RecordingDto(
                1,
                "2025-03-02T15:30:00",
                480,
                "Piano",
                notes);

        RecordingDto recording2 = new RecordingDto(
                2,
                "2025-03-02T15:30:00",
                480,
                "Piano",
                notes);

        assertFalse(recording1.equals(recording2));
        assertNotEquals(recording1.hashCode(), recording2.hashCode());
    }

    @Test
    void toString_RecordingDto_CorrectFormat() {
        List<NoteDto> notes = new ArrayList<>();
        notes.add(new NoteDto(60, 0, 240, 240));

        RecordingDto recording = new RecordingDto(
                1,
                "2025-03-02T15:30:00",
                480,
                "Piano",
                notes);

        String expected = "RecordingDto[id=1, timestamp=2025-03-02T15:30:00, duration=480, instrument=Piano, notes=[NoteDto[midi=60, startTime=0, stopTime=240, duration=240]]]";

        assertEquals(expected, recording.toString());
    }

    @Test
    void notesList_Modification_DoesNotAffectOriginal() {
        List<NoteDto> originalNotes = new ArrayList<>();
        originalNotes.add(new NoteDto(60, 0, 240, 240));

        RecordingDto recording = new RecordingDto(
                1,
                "2025-03-02T15:30:00",
                480,
                "Piano",
                originalNotes);

        List<NoteDto> modifiedNotes = new ArrayList<>(recording.notes());
        modifiedNotes.add(new NoteDto(62, 240, 480, 240));

        assertEquals(originalNotes.size(), recording.notes().size());
        assertEquals(originalNotes, recording.notes());
    }
}
