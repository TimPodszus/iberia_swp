package de.uol.swp.common.player.message.event;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class DiscardPlayerCardEventTest {

    @Test
    void testConstructorAndGetters() {
        String lobbyId = "testLobby";
        ICardDTO card = mock(ICardDTO.class);
        List<ICardDTO> cards = List.of(card);

        DiscardPlayerCardEvent event = new DiscardPlayerCardEvent(lobbyId, cards);

        assertEquals(lobbyId, event.getLobbyId());
        assertEquals(cards, event.getCards());
    }

    @Test
    void testEquals() {
        String lobbyId = "testLobby";
        ICardDTO card = mock(ICardDTO.class);
        List<ICardDTO> cards = List.of(card);

        DiscardPlayerCardEvent event1 = new DiscardPlayerCardEvent(lobbyId, cards);
        DiscardPlayerCardEvent event2 = new DiscardPlayerCardEvent(lobbyId, cards);
        DiscardPlayerCardEvent event3 = new DiscardPlayerCardEvent("differentLobby", cards);
        DiscardPlayerCardEvent event4 = new DiscardPlayerCardEvent(lobbyId, List.of(mock(ICardDTO.class)));

        // Test equality with itself
        assertEquals(event1, event1);

        // Test equality with another object with the same values
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());

        // Test inequality with null
        assertNotEquals(event1, null);

        // Test inequality with an object of a different class
        assertNotEquals(event1, new Object());

        // Test inequality with different lobbyId
        assertNotEquals(event1, event3);

        // Test inequality with different cards
        assertNotEquals(event1, event4);
    }

    @Test
    void testNotEquals() {
        String lobbyId1 = "testLobby1";
        String lobbyId2 = "testLobby2";
        ICardDTO card1 = mock(ICardDTO.class);
        ICardDTO card2 = mock(ICardDTO.class);
        List<ICardDTO> cards1 = List.of(card1);
        List<ICardDTO> cards2 = List.of(card2);

        DiscardPlayerCardEvent event1 = new DiscardPlayerCardEvent(lobbyId1, cards1);
        DiscardPlayerCardEvent event2 = new DiscardPlayerCardEvent(lobbyId2, cards2);

        assertNotEquals(event1, event2);
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }
}