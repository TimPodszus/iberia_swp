package de.uol.swp.server.cards.data.eventcards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForTheGoodCauseEventCardTest {
    @Test
    void testConstructor() {
        ForTheGoodCauseEventCard forTheGoodCauseEventCard = new ForTheGoodCauseEventCard(1);
        assertEquals(1, forTheGoodCauseEventCard.getId());
        assertEquals("Für den guten Zweck", forTheGoodCauseEventCard.getTitle());
        assertEquals("Du darfst dir eine Ereigniskarte aus dem Ablagestapel aussuchen", forTheGoodCauseEventCard.getDescription());
    }
}
