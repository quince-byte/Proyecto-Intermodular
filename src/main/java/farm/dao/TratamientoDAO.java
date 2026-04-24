package farm.dao;

import farm.model.Tratamiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TratamientoDAO {
    // Función para obtener un tratamiento por id de animal
    public List<Tratamiento> getByAnimalId(int animalId) {
        List<Tratamiento> tratamientos = new ArrayList<>();
        String query = "SELECT * FROM tratamientos WHERE animal_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, animalId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tratamientos.add(new Tratamiento(
                            rs.getInt("id"),
                            rs.getInt("animal_id"),
                            rs.getString("descripcion"),
                            rs.getDate("fecha").toLocalDate()));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo tratamientos: " + e.getMessage());
        }
        return tratamientos;
    }

    // Función para añadir un tratamiento a un animal
    public Tratamiento insert(Tratamiento tratamiento) {
        String query = "INSERT INTO tratamientos (animal_id, descripcion, fecha) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tratamiento.getAnimalId());
            ps.setString(2, tratamiento.getDescripcion());
            ps.setDate(3, Date.valueOf(tratamiento.getFecha()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    tratamiento.setId(rs.getInt(1));
                    return tratamiento;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error insertando tratamiento: " + e.getMessage());
        }
        return null;
    }
}
