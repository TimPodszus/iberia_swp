package de.uol.swp.common.game.message.event;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.player.IPlayerDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CardExchangeConfirmationEventTest {

    @Test
    void testEqualsAndHashCode() {
        IPlayerDTO player1 = mock(IPlayerDTO.class);
        IPlayerDTO player2 = mock(IPlayerDTO.class);
        ICardDTO card = mock(ICardDTO.class);

        CardExchangeConfirmationEvent event1 = new CardExchangeConfirmationEvent(
                "lobby1",
                true,
                player1,
                card,
                player2
        );
        CardExchangeConfirmationEvent event2 = new CardExchangeConfirmationEvent(
                "lobby1",
                true,
                player1,
                card,
                player2
        );
        CardExchangeConfirmationEvent event3 = new CardExchangeConfirmationEvent(
                "lobby2",
                false,
                player2,
                card,
                player1
        );

        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    void testConstructorAndGetters() {
        IPlayerDTO player1 = mock(IPlayerDTO.class);
        IPlayerDTO player2 = mock(IPlayerDTO.class);
        ICardDTO card = mock(ICardDTO.class);

        CardExchangeConfirmationEvent event = new CardExchangeConfirmationEvent("lobby1", true, player1, card, player2);

        assertEquals("lobby1", event.getLobbyId());
        assertTrue(event.isGiveRequest());
        assertEquals(player1, event.getRequestingPlayer());
        assertEquals(card, event.getRequestingCard());
        assertEquals(player2, event.getReceivingPlayer());
    }
}