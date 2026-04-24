package farm.dao;

import farm.model.Granja;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GranjaDAO {
    // Función para obtener todas las granjas
    public List<Granja> getAll() {
        List<Granja> granjas = new ArrayList<>();
        String query = "SELECT * FROM granjas";
        try (Connection con = DBConnection.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                granjas.add(new Granja(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getString("propietario")));
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo granjas: " + e.getMessage());
        }
        return granjas;
    }

    // Función para obtener una granja por nombre
    public Granja getByNombre(String nombre) {
        String query = "SELECT * FROM granjas WHERE nombre = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Granja(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("ubicacion"),
                            rs.getString("propietario"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error buscando granja: " + e.getMessage());
        }
        return null;
    }

    // Función para insertar una granja
    public Granja insert(Granja granja) {
        String query = "INSERT INTO granjas (nombre, ubicacion, propietario) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, granja.getNombre());
            ps.setString(2, granja.getUbicacion());
            ps.setString(3, granja.getPropietario());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    granja.setId(rs.getInt(1));
                    return granja;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error insertando granja: " + e.getMessage());
        }
        return null;
    }
}
