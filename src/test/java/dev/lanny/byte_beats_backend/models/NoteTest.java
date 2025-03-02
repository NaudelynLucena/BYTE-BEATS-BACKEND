package dev.lanny.byte_beats_backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

public class NoteTest {
    private Note note;

    @BeforeEach
    public void setUp() {
        note = new Note(60, 1000L, 2000L, 1000L);
    }

    @Test
    @DisplayName("Validamos que devuelva el MIDI correcto")
    public void test_Get_Midi() {
        assertThat("El MIDI debería ser 60", note.getMidi(), is(equalTo(60)));
    }

    @Test
    @DisplayName("Validamos que devuelva el startTime correcto")
    public void test_Get_StartTime() {
        assertThat("El startTime debería ser 1000", note.getStartTime(), is(equalTo(1000L)));
    }

    @Test
    @DisplayName("Validamos que devuelva el stopTime correcto")
    public void test_Get_StopTime() {
        assertThat("El stopTime debería ser 2000", note.getStopTime(), is(equalTo(2000L)));
    }

    @Test
    @DisplayName("Validamos que devuelva la duración correcta")
    public void test_Get_Duration() {
        assertThat("La duración debería ser 1000", note.getDuration(), is(equalTo(1000L)));
    }

    @Test
    @DisplayName("Validamos que devuelva el toString correcto")
    public void test_ToString() {
        String expectedString = "Note{" +
            "midi=60, " +
            "startTime=1000, " +
            "stopTime=2000, " +
            "duration=1000" +
            '}';
        assertThat("El método toString() debe coincidir con el formato esperado",
            note.toString(), is(equalTo(expectedString)));
    }
}
