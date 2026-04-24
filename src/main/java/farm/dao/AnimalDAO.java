package farm.dao;

import farm.model.Animal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAO {
    // Función para obtener los animales
    public List<Animal> getAll() {
        List<Animal> animales = new ArrayList<>();
        String query = "SELECT a.*, c.nombre as corral_nombre, g.nombre as granja_nombre " +
                "FROM animales a " +
                "LEFT JOIN corrales c ON a.corral_id = c.id " +
                "LEFT JOIN granjas g ON c.granja_id = g.id";

        try (Connection con = DBConnection.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Animal animal = new Animal(
                        rs.getInt("id"),
                        rs.getString("numero_crotal"),
                        rs.getString("especie"),
                        rs.getString("raza"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getDouble("peso_kg"),
                        rs.getString("estado_salud"),
                        rs.getString("proposito"),
                        rs.getInt("corral_id"));
                animal.setCorralNombre(rs.getString("corral_nombre"));
                animal.setGranjaNombre(rs.getString("granja_nombre"));
                animales.add(animal);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo animales: " + e.getMessage());
        }
        return animales;
    }

    // Función para insertar un animal
    public void insert(Animal a) throws SQLException {
        String query = "INSERT INTO animales (corral_id, numero_crotal, especie, raza, fecha_nacimiento, peso_kg, estado_salud, proposito) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, a.getCorralId());
            ps.setString(2, a.getNumeroCrotal());
            ps.setString(3, a.getEspecie());
            ps.setString(4, a.getRaza());
            ps.setDate(5, Date.valueOf(a.getFechaNacimiento()));
            ps.setDouble(6, a.getPesoKg());
            ps.setString(7, a.getEstadoSalud());
            ps.setString(8, a.getProposito());
            ps.executeUpdate();

        }
    }

    // Función para actualizar un animal
    public void update(Animal a) throws SQLException {
        String query = "UPDATE animales SET numero_crotal=?, especie=?, raza=?, fecha_nacimiento=?, peso_kg=?, estado_salud=?, proposito=?, corral_id=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, a.getNumeroCrotal());
            ps.setString(2, a.getEspecie());
            ps.setString(3, a.getRaza());
            ps.setDate(4, Date.valueOf(a.getFechaNacimiento()));
            ps.setDouble(5, a.getPesoKg());
            ps.setString(6, a.getEstadoSalud());
            ps.setString(7, a.getProposito());
            ps.setInt(8, a.getCorralId());
            ps.setInt(9, a.getId());
            ps.executeUpdate();

        }
    }

    // Función para eliminar un animal
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM animales WHERE id=?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        }
    }
}
