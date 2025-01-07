package de.uol.swp.server.connection;

import de.uol.swp.server.city.CityName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void testGetConnectionByIDMethod() {
        Connection connection = repository.getConnectionByID(1);
        assertNotNull(connection);
        assertEquals(1, connection.getId());
        assertEquals(List.of(CityName.A_CORUNA, CityName.GIJON), connection.getCityNames());

        Connection connection28 = repository.getConnectionByID(28);
        assertNotNull(connection28);
        assertEquals(28, connection28.getId());
        assertEquals(List.of(CityName.CADIZ, CityName.GIBRALTAR), connection28.getCityNames());
    }
}