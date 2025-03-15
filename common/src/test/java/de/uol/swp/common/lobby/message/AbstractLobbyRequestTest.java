package de.uol.swp.common.lobby.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test for the abstract lobby request
 *
 * @see de.uol.swp.common.lobby.message.AbstractLobbyRequest
 * @since 2023-05-14
 */
class AbstractLobbyRequestTest {

    /**
     * Tests the equals and hashCode methods of AbstractLobbyRequest.
     */
    @Test
    void testEqualsAndHashCode() {
        AbstractLobbyRequest request1 = new AbstractLobbyRequest("lobby1");
        AbstractLobbyRequest request2 = new AbstractLobbyRequest("lobby1");
        AbstractLobbyRequest request3 = new AbstractLobbyRequest("lobby2");

        assertEquals(request1, request1);
        assertEquals(request1, request2);

        assertNotEquals(request1, request3);
        assertNotEquals(request1, null);
        assertNotEquals(request1, new Object());

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}
