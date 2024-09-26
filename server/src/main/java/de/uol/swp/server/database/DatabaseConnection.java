package de.uol.swp.server.database;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class DatabaseConnection {

    private static final Logger LOG = LogManager.getLogger(DatabaseConnection.class);

    // Singleton-Instanz
    private static DatabaseConnection instance;

    // Methode zum Abrufen der Datenbankverbindung
    private final Connection connection;
    private static final Dotenv dotenv = Dotenv.configure()
                                               .directory("./")
                                               .load();
    // Datenbankverbindungsinformationen
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/iberia_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = dotenv.get("MYSQL_ROOT_PASSWORD");

    // Privater Konstruktor, um eine Instanz zu verhindern
    private DatabaseConnection() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            LOG.error("Fehler beim Erstellen der Datenbankverbindung.", e);
            throw new SQLException("Fehler beim Erstellen der Datenbankverbindung.", e);
        }
    }


    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    private static synchronized DatabaseConnection createInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

}


