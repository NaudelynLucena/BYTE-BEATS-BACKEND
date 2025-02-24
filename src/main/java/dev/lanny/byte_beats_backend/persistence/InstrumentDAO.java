package dev.lanny.byte_beats_backend.persistence;


import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import dev.lanny.byte_beats_backend.models.Instruments;

public class InstrumentDAO {
    private final H2Database h2Database;    

    public InstrumentDAO(H2Database h2Database) {
        this.h2Database = h2Database;
    }
//Guardar un Instrumento en la BD
public void saveInstruments(Instruments instruments) {
        if (instruments == null ||
                instruments.getname() == null || instruments.getname().trim().isEmpty() ||
                instruments.getType() == null || instruments.getType().trim().isEmpty() ||
                instruments.getSound() == null || instruments.getSound().trim().isEmpty()) {

            throw new IllegalArgumentException("❌ No se puede insertar un instrumento con valores nulos o vacíos.");
        }

        String sql = "INSERT INTO instruments (id, name, type, sound) VALUES (?, ?, ?, ?)";
        try (Connection connection = h2Database.getConnection();
                PreparedStatement pStatement = connection.prepareStatement(sql)) {

            pStatement.setInt(1, instruments.getId());
            pStatement.setString(2, instruments.getname());
            pStatement.setString(3, instruments.getType());
            pStatement.setString(4, instruments.getSound());

            int affectedRows = pStatement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("⚠️ No se insertó ningún registro en la base de datos.");
            } else {
                System.out.println("✅ Instrumento guardado correctamente: " + instruments);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el instrumento en la base de datos.", e);
        }
    }

    public List<Instruments> loadInstruments() {
        List<Instruments> instruments = new ArrayList<>();
        String sql = "SELECT * FROM instruments";
    
        try (Connection connection = h2Database.getConnection()) {
            Statement statement = connection.createStatement();
            try (ResultSet resultSet = statement.executeQuery(sql)) {
    
                System.out.println("Cargando instrumentos desde la base de datos...");
                while (resultSet.next()) {
                    Instruments instrument = new Instruments(
                            resultSet.getString("name"),
                            resultSet.getInt("id"),
                            resultSet.getString("type"),
                            resultSet.getString("sound"));
                    instruments.add(instrument);
                }
            } finally {
                statement.close();
            }
    
        } catch (SQLException e) {
            System.out.println("ERROR al recuperar los instrumentos: " + e.getMessage());
            e.printStackTrace();
        }
    
        return instruments;
    }

    public boolean deleteInstruments(int id) {
        String sql = "DELETE FROM instruments WHERE id = ?";
        try (Connection conn = h2Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println("🔍 Intentando eliminar el instrumento con ID: " + id);

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            System.out.println("Filas afectadas después de eliminar: " + affectedRows);

            return affectedRows > 0; // Devuelve true si se eliminó un registro
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateInstruments(int id, String newName, String newType, String newSoundFile) {
        String sql = "UPDATE instruments SET name = ?, type = ?, sound = ? WHERE id = ?";
        try (Connection conn = h2Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newType);
            pstmt.setString(3, newSoundFile);
            pstmt.setInt(4, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Devuelve true si se actualizó algún registro
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
