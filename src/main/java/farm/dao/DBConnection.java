package farm.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "farm_db";
    private static final String URL = BASE_URL + DB_NAME;
    private static final String USER = "root";
    private static final String PASSWORD = "mysql";

    // Función para crear la base de datos si no existe y conectarse
    public static void initializeDatabase() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            con.isValid(2);
            System.out.println("Conexión establecida con éxito.");
        } catch (SQLException e) {
            System.out.println("Base de datos no encontrada. Creándola...");
            executeSqlScript("sql/farm_db.sql");
        }
    }

    // Función para ejecutar un script
    private static void executeSqlScript(String filePath) {
        try (Connection con = DriverManager.getConnection(BASE_URL, USER, PASSWORD);
                Statement stmt = con.createStatement()) {

            String sqlContent = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] statements = sqlContent.split(";");

            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement.trim());
                }
            }
            System.out.println("Script SQL ejecutado correctamente.");
        } catch (SQLException | IOException ex) {
            System.err.println("Error ejecutando el script SQL: " + ex.getMessage());
        }
    }

    // Función para obtener la conexión
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
