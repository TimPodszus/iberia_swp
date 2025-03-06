package de.uol.swp.server.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Singleton class to manage the database connection using HikariCP.
 */
@Getter
public class DatabaseConnection {


    private static final Dotenv dotenv = Dotenv.configure()
                                               .directory("./")
                                               .load();
    private static final String JDBC_URL = "jdbc:mysql://db:3306";
    private static final String USERNAME = "root";
    private static final String PASSWORD = dotenv.get("MYSQL_ROOT_PASSWORD");

    private static DatabaseConnection instance;
    private final HikariDataSource dataSource;

    /**
     * Private constructor to create a database connection pool.
     */
    private DatabaseConnection() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(60000);
        config.setConnectionTimeout(30000);
        config.setLeakDetectionThreshold(2000);

        this.dataSource = new HikariDataSource(config);
    }

    /**
     * Returns the singleton instance of the DatabaseConnection.
     *
     * @return the singleton instance
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    /**
     * Creates the singleton instance of the DatabaseConnection in a thread-safe manner.
     *
     * @return the singleton instance
     */
    private static synchronized DatabaseConnection createInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Gets a connection from the pool.
     *
     * @return a database connection
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}