package de.uol.swp.server.database;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Singleton-Instanz
    private static DatabaseConnection instance;
    // Methode zum Abrufen der Datenbankverbindung
    @Getter
    private Connection connection;
    private static final Dotenv dotenv = Dotenv.configure().directory("./").load();
    // Datenbankverbindungsinformationen
    private final String jdbcUrl = "jdbc:mysql://localhost:3306/iberia_db";
    private final String username = "root";
    private final String password = dotenv.get("MYSQL_ROOT_PASSWORD");

    // Privater Konstruktor, um eine Instanz zu verhindern
    private DatabaseConnection() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Fehler beim Erstellen der Datenbankverbindung.", e);
        }
    }

    // Methode zum Abrufen der Singleton-Instanz
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

}


