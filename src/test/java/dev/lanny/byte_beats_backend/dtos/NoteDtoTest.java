package dev.lanny.byte_beats_backend.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NoteDtoTest {

    @Test
    void createNoteDto_CorrectFields_NoteCreatedSuccessfully() {

        NoteDto note = new NoteDto(
            60,
            0,
            480,
            480
        );

        assertEquals(60, note.midi());
        assertEquals(0, note.startTime());
        assertEquals(480, note.stopTime());
        assertEquals(480, note.duration());
    }

    @Test
    void compareNoteDtos_SameValues_AreEqual() {
        NoteDto note1 = new NoteDto(60, 0, 480, 480);
        NoteDto note2 = new NoteDto(60, 0, 480, 480);

        assertTrue(note1.equals(note2));
        assertEquals(note1.hashCode(), note2.hashCode());
    }

    @Test
    void compareNoteDtos_DifferentValues_NotEqual() {
        NoteDto note1 = new NoteDto(60, 0, 480, 480);
        NoteDto note2 = new NoteDto(61, 0, 480, 480);

        assertFalse(note1.equals(note2));
        assertNotEquals(note1.hashCode(), note2.hashCode());
    }

    @Test
    void toString_NoteDto_CorrectFormat() {
        NoteDto note = new NoteDto(60, 0, 480, 480);
        String expected = "NoteDto[midi=60, startTime=0, stopTime=480, duration=480]";

        assertEquals(expected, note.toString());
    }
}
