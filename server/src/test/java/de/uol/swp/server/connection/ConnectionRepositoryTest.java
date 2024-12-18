package de.uol.swp.server.connection;

import de.uol.swp.server.city.CityName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectionRepositoryTest {
    static ConnectionRepository repository;
    static List<Connection> connections;

    @BeforeAll
    static void create() {
        repository = new ConnectionRepository();
        connections = repository.getConnections();
    }


    @Test
    void testCreateAllConnections() {
        assertEquals(81, connections.size());
    }

    @Test
    void testGetConnectionByID() {
        Connection connection1 = connections.get(0);
        assertEquals(1, connection1.getId());
        assertTrue(connection1.isTrainTrackBuildable());
        assertEquals(List.of(CityName.A_CORUNA, CityName.GIJON), connection1.getCityNames());

        Connection connection28 = connections.get(27);
        assertEquals(28, connection28.getId());
        assertFalse(connection28.isTrainTrackBuildable());
        assertEquals(List.of(CityName.CADIZ, CityName.GIBRALTAR), connection28.getCityNames());
    }
}