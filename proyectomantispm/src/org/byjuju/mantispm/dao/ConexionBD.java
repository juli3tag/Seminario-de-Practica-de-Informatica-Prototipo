package org.byjuju.mantispm.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConexionBD: centraliza la conexión JDBC.
 * Permite cambiar parámetros de conexión según el entorno (producción / test).
 */
public class ConexionBD {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/mantis_pm?serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "hola";

    public static Connection conectar() throws SQLException {
        String env = System.getProperty("env");
        if ("test".equalsIgnoreCase(env)) {
            try (InputStream in = ConexionBD.class.getResourceAsStream("/test-config.properties")) {
                if (in != null) {
                    Properties props = new Properties();
                    props.load(in);
                    String url = props.getProperty("jdbc.url");
                    String user = props.getProperty("jdbc.user");
                    String pass = props.getProperty("jdbc.password");
                    return DriverManager.getConnection(url, user, pass);
                } else {
                    System.err.println("test-config.properties no encontrado; usando configuración por defecto.");
                }
            } catch (Exception ex) {
                System.err.println("Error leyendo test-config.properties: " + ex.getMessage());
            }
        }
        return DriverManager.getConnection(DEFAULT_URL, DEFAULT_USER, DEFAULT_PASSWORD);
    }
}
