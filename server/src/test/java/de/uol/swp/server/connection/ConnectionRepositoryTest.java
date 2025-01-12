package de.uol.swp.server.connection;

import de.uol.swp.server.city.CityName;
import de.uol.swp.server.connection.data.Connection;
import de.uol.swp.server.connection.data.IConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link ConnectionRepository}.
 * This class verifies the functionality of creating and retrieving {@link Connection} objects from the repository.
 */
class ConnectionRepositoryTest {
    /**
     * Static instance of the {@link ConnectionRepository} used for testing.
     */
    static ConnectionRepository repository;

    /**
     * Static list of {@link Connection} objects retrieved from the repository.
     */
    static List<IConnection> connections;

    /**
     * Initializes the {@link ConnectionRepository} and retrieves all connections before all test methods are executed.
     */
    @BeforeAll
    static void create() {
        repository = new ConnectionRepository();
        connections = repository.getConnections();
    }

    /**
     * Tests that the repository creates all expected connections.
     * Verifies that the size of the connection list matches the expected number of connections.
     */
    @Test
    void testCreateAllConnections() {
        assertEquals(81, connections.size());
    }

    /**
     * Tests the {@link ConnectionRepository#getConnectionByID(int)} method.
     * Verifies that connections with specific IDs can be retrieved correctly, and that their details match expectations.
     */
    @Test
    void testGetConnectionByID() {
        IConnection connection1 = repository.getConnectionByID(1);
        assertEquals(1, connection1.getId(), "Expected ID 1 for connection 1");
        assertTrue(connection1.isTrainTrackBuildable(), "Expected train track to be buildable for connection 1");
        assertEquals(
                List.of(CityName.A_CORUNA, CityName.GIJON),
                connection1.getCityNames(),
                "Expected cities A Coruna and Gijon for connection 1"
        );

        IConnection connection28 = repository.getConnectionByID(28);
        assertEquals(28, connection28.getId(), "Expected ID 28 for connection 28");
        assertFalse(connection28.isTrainTrackBuildable(), "Expected train track not to be buildable for connection 28");
        assertEquals(
                List.of(CityName.CADIZ, CityName.GIBRALTAR),
                connection28.getCityNames(),
                "Expected cities Cadiz and Gibraltar for connection 28"
        );
    }

    /**
     * Tests getting connections of a specific city.
     */
    @Test
    void testGetConnectionsOfCity() {
        List<IConnection> connectionsOfCity = repository.getConnectionsOfCity(CityName.A_CORUNA);
        assertEquals(2, connectionsOfCity.size(), "Expected 2 connections for A Coruna");
        assertTrue(
                connectionsOfCity.stream()
                                 .anyMatch(connection -> connection.getCityNames()
                                                                   .contains(CityName.GIJON)),
                "Expected connection to Gijon"
        );
        assertTrue(
                connectionsOfCity.stream()
                                 .anyMatch(connection -> connection.getCityNames()
                                                                   .contains(CityName.SANTIAGO_DE_COMPOSTELA)),
                "Expected connection to Santiago de Compostela"
        );
    }
}