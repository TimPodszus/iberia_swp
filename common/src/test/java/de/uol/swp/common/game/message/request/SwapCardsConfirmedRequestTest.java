package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.event.SwapCardsConfirmationEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwapCardsConfirmedRequestTest {

    @Test
    void testEqualsAndHashCode() {
        SwapCardsConfirmationEvent event1 = new SwapCardsConfirmationEvent("lobby1", "player1", "player2", null, null);
        SwapCardsConfirmationEvent event2 = new SwapCardsConfirmationEvent("lobby1", "player1", "player2", null, null);

        SwapCardsConfirmedRequest request1 = new SwapCardsConfirmedRequest("lobby1", true, event1);
        SwapCardsConfirmedRequest request2 = new SwapCardsConfirmedRequest("lobby1", true, event1);
        SwapCardsConfirmedRequest request3 = new SwapCardsConfirmedRequest("lobby1", false, event2);

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testGetters() {
        SwapCardsConfirmationEvent event = new SwapCardsConfirmationEvent("lobby1", "player1", "player2", null, null);
        SwapCardsConfirmedRequest request = new SwapCardsConfirmedRequest("lobby1", true, event);

        assertEquals("lobby1", request.getLobbyId());
        assertTrue(request.isAccepted());
        assertEquals(event, request.getEvent());
    }
}