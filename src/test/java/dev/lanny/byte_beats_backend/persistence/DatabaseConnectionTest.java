package dev.lanny.byte_beats_backend.persistence;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    @BeforeAll
    static void setup() {

        System.setProperty("db.url", "jdbc:h2:file:./data/musicaDB");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "");
    }

    @BeforeEach
    void resetConnection() throws SQLException {
        DatabaseConnection.closeConnection();
    }

    @Test
    void getConnection_ExistingConnection_ReturnsSameConnection() throws SQLException {

        Connection firstConn = DatabaseConnection.getConnection();

        Connection secondConn = DatabaseConnection.getConnection();

        assertSame(firstConn, secondConn);
    }

    @Test
    void getConnection_ClosedConnection_CreatesNewConnection() throws SQLException {

        Connection conn = DatabaseConnection.getConnection();
        DatabaseConnection.closeConnection();

        Connection newConn = DatabaseConnection.getConnection();

        assertNotNull(newConn);
        assertNotSame(conn, newConn);
        assertFalse(newConn.isClosed());
    }

    @Test
    void closeConnection_ClosesExistingConnection() throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        DatabaseConnection.closeConnection();

        assertTrue(conn.isClosed());
    }

}
