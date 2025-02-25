package de.uol.swp.server.cards.data.eventcards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OnTheMoveDayAndNightEventCardTest {

    @Test
    public void testConstructor() {
        OnTheMoveDayAndNightEventCard onTheMoveDayAndNightEventCard = new OnTheMoveDayAndNightEventCard(1);

        assertEquals(1, onTheMoveDayAndNightEventCard.getId());
        assertEquals("Tag und Nacht unterwegs", onTheMoveDayAndNightEventCard.getTitle());
        assertEquals("Du darfst dich auf dem Spielplan frei bewegen.", onTheMoveDayAndNightEventCard.getDescription());
    }
}
