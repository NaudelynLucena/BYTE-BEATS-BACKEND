package dev.lanny.byte_beats_backend.persistence;

import java.sql.*;
import java.util.*;

import dev.lanny.byte_beats_backend.models.Instruments;

public class H2Database {
    private static final String DB_URL = "jdbc:h2:mem:byte_beats; DB_CLOSE_DELAY=-1"; //edfino la conexion a la base de datos indicando que estara en memoria y mantiene la la base de datos activa mientras se ejecuta la aplicacion
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS instruments (id VARCHAR(255) PRIMARY KEY, name VARCHAR(255), type VARCHAR(255), soundFile VARCHAR(255))";

    public H2Database() {
        try (Connection conection = DriverManager.getConnection(DB_URL);// abro una conexion con la base de datos
                Statement statement = conection.createStatement()) {        //creo un objeto statement para ejecutar sentencias sql
            statement.execute(CREATE_TABLE_SQL); //ejecuto la sentencia sql para crear la tabla

        } catch (SQLException e) {  
            e.printStackTrace();    
        }
    }


    public void saveInstruments(Instruments instruments) {
        String sql = "INSERT INTO instruments (id, name, type, sound) VALUES (?, ?, ?, ?)"; //sentencia sql para insertar datos en la tabla instruments y uso parámetros ? para evitar inyecciones SQL.

        try (Connection connection = DriverManager.getConnection(DB_URL);   //abro una conexion con la base de datos
                PreparedStatement pStatement = connection.prepareStatement(sql)) {  //creo un objeto PreparedStatement para ejecutar sentencias sql
                    pStatement.setInt(1,instruments.getId());   //asigno los valores a los parametros de la sentencia sql,es decir se rellenan con los valores con pstmt.setString(), tomando datos del objeto InstrumentBase
                    pStatement.setString(2,instruments.getname());
                    pStatement.setString(3,instruments.getType());
                    pStatement.setString(4,instruments.getSound());
                    pStatement.executeUpdate(); //ejecuto la sentencia sql
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List <Instruments> loadInstruments() {   //metodo para cargar los datos de la tabla instruments
        List<Instruments> instruments = new ArrayList<>();    //creo la lista vacia  para lamacenar los instrumentos
        String sql = "SELECT * FROM instruments";   //ejecuta la consulta SELECT * FROM instruments para obtener todos los registros.

        try (Connection connection = DriverManager.getConnection(DB_URL);   
                Statement statement = connection.createStatement(); 
                ResultSet resultSet = statement.executeQuery(sql)) {    //creo un objeto ResultSet para almacenar los resultados de la consulta
                    while (resultSet.next()) {   //recorro los resultados de la consulta
                        instruments.add(new Instruments(resultSet.getString("name"), resultSet.getInt("id"), resultSet.getString("type"), resultSet.getString("sound")));   //creo el objeto Instruments con los datos obtenidos de la consulta y lo añado a la lista
                    }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instruments;
    }
}
