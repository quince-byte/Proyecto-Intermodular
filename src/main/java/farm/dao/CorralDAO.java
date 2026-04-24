package farm.dao;

import farm.model.Corral;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CorralDAO {
    // Función para obtener los datos de una granja por id
    public List<Corral> getByGranjaId(int granjaId) {
        List<Corral> corrales = new ArrayList<>();
        String query = "SELECT * FROM corrales WHERE granja_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, granjaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    corrales.add(new Corral(
                            rs.getInt("id"),
                            rs.getInt("granja_id"),
                            rs.getString("nombre"),
                            rs.getInt("capacidad")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo corrales: " + e.getMessage());
        }
        return corrales;
    }

    // Función para obtener una granja por nombre e id
    public Corral getByNombreAndGranja(String nombre, int granjaId) {
        String query = "SELECT * FROM corrales WHERE nombre = ? AND granja_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nombre);
            ps.setInt(2, granjaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Corral(
                            rs.getInt("id"),
                            rs.getInt("granja_id"),
                            rs.getString("nombre"),
                            rs.getInt("capacidad"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error buscando corral: " + e.getMessage());
        }
        return null;
    }

    // Función para insertar un corral
    public Corral insert(Corral corral) {
        String query = "INSERT INTO corrales (granja_id, nombre, capacidad) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, corral.getGranjaId());
            ps.setString(2, corral.getNombre());
            ps.setInt(3, corral.getCapacidad());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    corral.setId(rs.getInt(1));
                    return corral;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error insertando corral: " + e.getMessage());
        }
        return null;
    }
}
