package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.event.CardExchangeConfirmationEvent;
import de.uol.swp.common.player.IPlayerDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CardExchangeConfirmationRequestTest {

    @Test
    void testEquals() {
        CardExchangeConfirmationEvent event1 = new CardExchangeConfirmationEvent(
                "lobbyId",
                mock(IPlayerDTO.class),
                mock(ICardDTO.class),
                mock(IPlayerDTO.class)
        );
        CardExchangeConfirmationEvent event2 = new CardExchangeConfirmationEvent(
                "lobbyId",
                mock(IPlayerDTO.class),
                mock(ICardDTO.class),
                mock(IPlayerDTO.class)
        );
        CardExchangeConfirmationRequest request1 = new CardExchangeConfirmationRequest("lobby1", true, event1);
        CardExchangeConfirmationRequest request2 = new CardExchangeConfirmationRequest("lobby1", true, event1);
        CardExchangeConfirmationRequest request3 = new CardExchangeConfirmationRequest("lobby1", true, event2);

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

    @Test
    void testHashCode() {
        CardExchangeConfirmationEvent event1 = new CardExchangeConfirmationEvent(
                "lobbyId",

                mock(IPlayerDTO.class), mock(ICardDTO.class), mock(IPlayerDTO.class)
        );
        CardExchangeConfirmationEvent event2 = new CardExchangeConfirmationEvent(
                "lobbyId",

                mock(IPlayerDTO.class), mock(ICardDTO.class), mock(IPlayerDTO.class)
        );
        CardExchangeConfirmationRequest request1 = new CardExchangeConfirmationRequest("lobby1", true, event1);
        CardExchangeConfirmationRequest request2 = new CardExchangeConfirmationRequest("lobby1", true, event1);
        CardExchangeConfirmationRequest request3 = new CardExchangeConfirmationRequest("lobby1", true, event2);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testConstructorAndGetters() {
        CardExchangeConfirmationEvent event = new CardExchangeConfirmationEvent(
                "lobbyId",

                mock(IPlayerDTO.class), mock(ICardDTO.class), mock(IPlayerDTO.class)
        );
        CardExchangeConfirmationRequest request = new CardExchangeConfirmationRequest("lobby1", true, event);

        assertEquals("lobby1", request.getLobbyId());
        assertTrue(request.isAccepted());
        assertEquals(event, request.getCardExchangeConfirmationEvent());
    }
}