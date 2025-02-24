package dev.lanny.byte_beats_backend.persistence;

import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.lanny.byte_beats_backend.models.Instruments;
import dev.lanny.byte_beats_backend.models.Piano;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;

public class H2DatabaseTest {

    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private H2Database database;
    private Instruments piano;

    @BeforeEach
    public void setUp() {
        database = new H2Database(TEST_DB_URL);
        database.initDatabase();
        piano = new Piano(1, "Piano de Cola", "Cuerda", "piano.mp3", 88);

        // Eliminnamos lso registros antes de cada prueba
        try (

                Connection connection = DriverManager.getConnection(TEST_DB_URL);
                Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM instruments");
            System.out.println("BAse de Datos limpiada antes de la prueba");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Se debe inicializar la base de datos correctamente")
    void testInitDatabase() {
        assertDoesNotThrow(() -> database.initDatabase());
    }

    @Test
    @DisplayName("Debe lanzar una excepcion si no se puede inicializar la base de datos")
    void testInitDatabase_ExceptionError() {

        assertThrows(RuntimeException.class, () -> {
            new H2Database("jdbc:h2:invalid:url"); // URL inválida para simular error
        }, "Se esperaba que initDatabase lanzara una excepción por URL inválida.");
    }
/* 
    @Test
    @DisplayName("Se debe guardar un instrumento en la base de datos")
    void testInstruments_Save() {

        database.saveInstruments(piano);

        List<Instruments> instruments = database.loadInstruments();
        System.out.println("Instrumentos en la base de datos: " + instruments);
        assertThat(instruments, is(not(empty())));
    }

    @Test
    @DisplayName("Se debe insertar multiples instrumentos correctamente en la base de datos")
    void testInstruments_SaveMultiple() {
        database.saveInstruments(piano);
        database.saveInstruments(new Piano(2, "Piano electrónico", "Cuerda", "piano1.mp3", 88));
        database.saveInstruments(new Piano(3, "Piano de pared", "Cuerda", "piano2.mp3", 88));

        List<Instruments> instruments = database.loadInstruments();
        assertThat(instruments, hasSize(3));
    }

    @Test
    @DisplayName("Debe lanzar una excepción si se intenta guardar un instrumento con valores nulos")
    void testIntruments_Save_WithNullValues() {
        Instruments invalidInstrument = new Instruments(null, 1, null, null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            database.saveInstruments(invalidInstrument);
        });
        assertThat(exception.getMessage(),
                containsString("❌ No se puede insertar un instrumento con valores nulos o vacíos."));
    }

    @Test
    @DisplayName("Debe manejar correctamente cuando la inserción de un instrumento falla")
    void testIntruments_Save_InsertFailure() {

        Instruments invalidInstrument = new Instruments("", 1, "", "");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            database.saveInstruments(invalidInstrument);
        });

        assertThat(exception.getMessage(),containsString("No se puede insertar un instrumento con valores nulos o vacíos."));
        List<Instruments> instruments = database.loadInstruments();

        assertThat("No se debería haber insertado ningún instrumento.", instruments, hasSize(0));
    }

    @Test
    @DisplayName("Se debe recuperar un instrumento correctamente de la base de datos")
    void testInstruments_Load() {
        database.saveInstruments(piano);

        List<Instruments> instruments = database.loadInstruments();
        assertThat(instruments.get(0).getname().trim(), is("Piano de Cola"));
    }

    @Test
    @DisplayName("Se debe eliminar un instrumento de la base de datos correctamente")
    void testInstruments_Delete() {
        database.saveInstruments(piano);

        boolean deleted = database.deleteInstruments(piano.getId());
        assertTrue(deleted);
    }

    @Test
    @DisplayName("Se debe verificar que la lista de instrumentos esta vacia despues de haber eliminado un instrumento")
    void testInstruments_ListEmpty_After_Deletion() {
        database.saveInstruments(piano);

        database.deleteInstruments(piano.getId());
        List<Instruments> instruments = database.loadInstruments();
        assertThat(instruments, hasSize(0)); // Verificamos que la bd esta vacia
    }

    @Test
    @DisplayName("Se debe actualizar un isntrumento correctamente en la base de datos")
    void testInstruments_Update() {
        database.saveInstruments(piano);
        boolean updated = database.updateInstruments(1, "Piano de Cola", "Cuerda", "piano_new.mp3");
        assertTrue(updated);

        List<Instruments> instruments = database.loadInstruments();
        assertThat(instruments, hasSize(1));
        assertThat(instruments.get(0).getname(), is("Piano de Cola"));
        assertThat(instruments.get(0).getType(), is("Cuerda"));
        assertThat(instruments.get(0).getSound(), is("piano_new.mp3"));
    }

    @Test
    @DisplayName("debe manejar la eliminacion de un isntrumento inexistente")
    void testIntruments_Delete_NonExistent() {
        boolean deleted = database.deleteInstruments(999);
        assertFalse(deleted, "Se ha intentado eliminar un instrumento inexistente y el metodo no devolvio nada.");
    }

    @Test
    @DisplayName("debe manejar la actualizacion de un isntrumento inexistente")
    void testIntruments_Update_NonExistent() {
        boolean updated = database.updateInstruments(999, "Nuevo", "Nuevo", "nuevo.mp3");
        assertFalse(updated, "Se ha intentado actualizar un instrumento inexistente y el metodo no devolvio nada.");
    }

    @Test
    @DisplayName("debe manejar que s einserte un instrumento invalido")
    void testInstruments_Save_Invalid() {
        Instruments invalidInstrument = new Piano(1, null, null, null, 88);
        assertThrows(Exception.class, () -> database.saveInstruments(invalidInstrument),
                "Se ha intentado insertar un instrumento invalido y no se ha lanzado ninguna excepcion.");
    }
*/
}
