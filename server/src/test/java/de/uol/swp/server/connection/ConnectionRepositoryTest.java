package de.uol.swp.server.connection;

import de.uol.swp.server.city.CityName;
import de.uol.swp.server.connection.data.IConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ConnectionRepository.
 */
class ConnectionRepositoryTest {
    static ConnectionRepository repository;
    static List<IConnection> connections;

    /**
     * Initializes the repository and connections before all tests.
     */
    @BeforeAll
    static void create() {
        repository = new ConnectionRepository();
        connections = repository.getConnections();
    }

    /**
     * Tests that all connections are created.
     */
    @Test
    void testCreateAllConnections() {
        assertEquals(81, connections.size());
    }

    /**
     * Tests getting a connection by its ID.
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