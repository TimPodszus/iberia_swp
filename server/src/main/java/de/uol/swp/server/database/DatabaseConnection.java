package de.uol.swp.server.database;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class to manage the database connection.
 */
@Getter
public class DatabaseConnection {

    private static final Logger LOG = LogManager.getLogger(DatabaseConnection.class);

    private static final Dotenv dotenv = Dotenv.configure()
                                               .directory("./")
                                               .load();
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/iberia_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = dotenv.get("MYSQL_ROOT_PASSWORD");

    private static DatabaseConnection instance;
    private final Connection connection;

    /**
     * Private constructor to create a database connection.
     *
     * @throws SQLException if a database access error occurs
     */
    private DatabaseConnection() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            LOG.error("Fehler beim Erstellen der Datenbankverbindung.", e);
            throw new SQLException("Fehler beim Erstellen der Datenbankverbindung.", e);
        }
    }

    /**
     * Returns the singleton instance of the DatabaseConnection.
     *
     * @return the singleton instance
     * @throws SQLException if a database access error occurs
     */
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    /**
     * Creates the singleton instance of the DatabaseConnection in a thread-safe manner.
     *
     * @return the singleton instance
     * @throws SQLException if a database access error occurs
     */
    private static synchronized DatabaseConnection createInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

}


