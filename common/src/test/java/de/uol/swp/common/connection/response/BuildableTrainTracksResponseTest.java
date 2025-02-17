package de.uol.swp.common.connection.response;

import de.uol.swp.common.connection.IConnectionDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BuildableTrainTracksResponseTest {

    @Test
    void testEqualsAndHashCode() {
        IConnectionDTO connection1 = mock(IConnectionDTO.class);
        IConnectionDTO connection2 = mock(IConnectionDTO.class);

        List<IConnectionDTO> connections1 = List.of(connection1, connection2);
        List<IConnectionDTO> connections2 = List.of(connection1, connection2);
        List<IConnectionDTO> differentConnections = List.of(connection1);

        BuildableTrainTracksResponse response1 = new BuildableTrainTracksResponse("lobbyId", true, connections1);
        BuildableTrainTracksResponse response2 = new BuildableTrainTracksResponse("lobbyId", true, connections2);
        BuildableTrainTracksResponse response3 = new BuildableTrainTracksResponse("lobbyId", true, differentConnections);

        assertEquals(response1, response2, "Responses with the same connections should be equal");
        assertEquals(response1.hashCode(), response2.hashCode(), "Hash codes should be the same for equal objects");

        assertNotEquals(response1, response3, "Responses with different connections should not be equal");
        assertNotEquals(response1.hashCode(), response3.hashCode(), "Hash codes should be different for non-equal objects");
    }

    @Test
    void testNotEqualsWithNullOrDifferentClass() {
        IConnectionDTO connection = mock(IConnectionDTO.class);
        List<IConnectionDTO> connections = List.of(connection);

        BuildableTrainTracksResponse response = new BuildableTrainTracksResponse("lobbyId", true, connections);

        assertNotEquals(response, null, "Response should not be equal to null");
        assertNotEquals(response, new Object(), "Response should not be equal to an object of a different class");
    }
}
