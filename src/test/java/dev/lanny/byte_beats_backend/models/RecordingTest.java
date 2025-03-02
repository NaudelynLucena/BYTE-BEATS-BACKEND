package dev.lanny.byte_beats_backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import java.util.ArrayList;
import java.util.List;

public class RecordingTest {
    private Recording recording;

    @BeforeEach
    public void setUp() throws Exception {
        List<Note> notes = new ArrayList<>();
        recording = new Recording(1, "title", "Piano", 120000L, notes);
    }

    @Test
    @DisplayName("Validamos que devuelva el ID correcto")
    public void test_Get_Id() {
        assertThat("El id debería ser 1", recording.getId(), is(equalTo(1)));
    }

    @Test
    @DisplayName("Validamos que devuelva el título correcto")
    public void test_Get_Title() {
        assertThat("El título debería ser 'title'", recording.getTitle(), is(equalTo("title")));
    }

    @Test
    @DisplayName("Validamos que devuelva el instrumento correcto")
    public void test_Get_Instrument() {
        assertThat("El instrumento debería ser 'Piano'", recording.getInstrument(), is(equalTo("Piano")));
    }

    @Test
    @DisplayName("Validamos que devuelva la duración correcta")
    public void test_Get_Duration() {
        assertThat("La duración debería ser 120000", recording.getDuration(), is(equalTo(120000L)));
    }

    @Test
    @DisplayName("Validamos que devuelva la lista de notas vacía")
    public void test_Get_Notes() {
        assertThat("La lista de notas debería estar vacía", recording.getNotes().isEmpty(), is(true));
    }

    @Test
    @DisplayName("Validamos que devuelva el toString correcto")
    public void test_ToString() {
        String expectedString = "Recording{" +
            "id=1, " +
            "title='title', " +
            "instrument='Piano', " +
            "duration=120000, " +
            "notes=[]" +
            '}';
        assertThat("El método toString() debe coincidir con el formato esperado",
            recording.toString(), is(equalTo(expectedString)));
    }
}
