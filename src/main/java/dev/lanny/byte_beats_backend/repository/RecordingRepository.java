package dev.lanny.byte_beats_backend.repository;

import java.sql.Connection;
import java.sql.SQLException;

import dev.lanny.byte_beats_backend.persistence.DatabaseConnection;

public class RecordingRepository {
    private final Connection connection;

    public RecordingRepository(){
        try {
            this.connection = DatabaseConnection.getConnection();
        
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("error al conectarse con la base de datos");
            

            
        }
    }

}
