package dev.lanny.byte_beats_backend.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import utils.DatabaseManager;

public class DatabaseManagerTest {

    @Test
    public void testGetConnection() {
        Connection connection = DatabaseManager.getConnection();
        assertNotNull(connection, "La conexión no debería ser nula");
    }

    @Test
    public void testCloseConnection() {
        DatabaseManager.closeConnection();
        Connection connection = DatabaseManager.getConnection();
        assertNotNull(connection, "Debería abrir una nueva conexión después de cerrar");
    }

    @Test
    public void testSingletonPattern() {
        Connection firstConnection = DatabaseManager.getConnection();
        Connection secondConnection = DatabaseManager.getConnection();
        assertEquals(firstConnection, secondConnection, "Ambas conexiones deberían ser la misma instancia");
    }
}
