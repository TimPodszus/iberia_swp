package de.uol.swp.common.connection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for ConnectionDTO.
 */
public class ConnectionDTOTest {

    /**
     * Tests the ConnectionDTO constructor and its getters.
     */
    @Test
    void testDto() {
        ConnectionDTO connectionDTO = new ConnectionDTO(1, List.of("city1", "city2"), true, true);
        assertEquals(1, connectionDTO.getId());
        assertEquals(connectionDTO.getCityNames(), List.of("city1", "city2"));
        assertTrue(connectionDTO.isTrainTrack());
        assertTrue(connectionDTO.isTrainTrackBuildable());
    }
}
