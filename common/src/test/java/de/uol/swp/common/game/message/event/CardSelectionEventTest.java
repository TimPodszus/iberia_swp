package de.uol.swp.common.game.message.event;

import de.uol.swp.common.cards.data.EventCardDTO;
import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for CardSelectionEvent.
 */
public class CardSelectionEventTest {

    /**
     * Tests the constructor of CardSelectionEvent.
     */
    @Test
    void testConstructor() {
        List<ICardDTO> cards = new ArrayList<>(List.of(new EventCardDTO(1, "", "")));
        CardSelectionEvent event = new CardSelectionEvent("lobbyId", cards);
        assertEquals("lobbyId", event.getLobbyId());
        assertEquals(cards, event.getCards());
    }

    /**
     * Tests the equals method of CardSelectionEvent with two identical objects.
     */
    @Test
    void testEquals() {
        List<ICardDTO> cards = new ArrayList<>(List.of(new EventCardDTO(1, "", "")));
        CardSelectionEvent event1 = new CardSelectionEvent("lobbyId", cards);
        CardSelectionEvent event2 = new CardSelectionEvent("lobbyId", cards);
        assertEquals(event1, event2);
    }

    /**
     * Tests the equals method of CardSelectionEvent with the same object.
     */
    @Test
    void testEqualsWithSameObject() {
        List<ICardDTO> cards = new ArrayList<>(List.of(new EventCardDTO(1, "", "")));
        CardSelectionEvent event = new CardSelectionEvent("lobbyId", cards);
        assertEquals(event, event);
    }

    /**
     * Tests the equals method of CardSelectionEvent with a different object.
     */
    @Test
    void testEqualsWithDifferentObject() {
        List<ICardDTO> cards = new ArrayList<>(List.of(new EventCardDTO(1, "", "")));
        CardSelectionEvent event = new CardSelectionEvent("lobbyId", cards);
        assertNotEquals(event, new Object());
    }

    /**
     * Tests the hashCode method of CardSelectionEvent.
     */
    @Test
    void testHashCode() {
        List<ICardDTO> cards = new ArrayList<>(List.of(new EventCardDTO(1, "", "")));
        CardSelectionEvent event1 = new CardSelectionEvent("lobbyId", cards);
        CardSelectionEvent event2 = new CardSelectionEvent("lobbyId", cards);
        assertEquals(event1.hashCode(), event2.hashCode());
    }
}
