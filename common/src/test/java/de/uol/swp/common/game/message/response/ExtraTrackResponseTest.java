package de.uol.swp.common.game.message.response;

import de.uol.swp.common.connection.IConnectionDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExtraTrackResponseTest {

    @Test
    void testConstructorAndGetters() {
        IConnectionDTO connectionMock = mock(IConnectionDTO.class);
        List<IConnectionDTO> connections = List.of(connectionMock);
        ExtraTrackResponse response = new ExtraTrackResponse("lobby123", true, connections);

        assertEquals("lobby123", response.getLobbyId());
        assertTrue(response.isSuccess());
        assertEquals(connections, response.getConnections());
    }

    @Test
    void testEqualsAndHashCode() {
        IConnectionDTO connectionMock1 = mock(IConnectionDTO.class);
        IConnectionDTO connectionMock2 = mock(IConnectionDTO.class);
        List<IConnectionDTO> connections1 = List.of(connectionMock1);
        List<IConnectionDTO> connections2 = List.of(connectionMock2);

        ExtraTrackResponse response1 = new ExtraTrackResponse("lobby123", true, connections1);
        ExtraTrackResponse response2 = new ExtraTrackResponse("lobby123", true, connections1);
        ExtraTrackResponse response3 = new ExtraTrackResponse("lobby123", true, connections2);
        ExtraTrackResponse response4 = new ExtraTrackResponse("lobby456", false, connections1);

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
        assertNotEquals(response1, response4);
        assertNotEquals(null, response1);
        assertNotEquals( new Object(), response1);
    }
}
