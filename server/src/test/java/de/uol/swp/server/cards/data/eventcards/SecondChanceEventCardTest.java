package de.uol.swp.server.cards.data.eventcards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecondChanceEventCardTest {
    @Test
    public void testConstructor() {
        SecondChanceEventCard secondChanceEventCard = new SecondChanceEventCard(1);

        assertEquals(1, secondChanceEventCard.getId());
        assertEquals("Zweite Chance", secondChanceEventCard.getTitle());
        assertEquals("Du bekommst die Stadtkarte deines momentanen Aufenthaltsort aus dem " +
                "Ablagestapel.", secondChanceEventCard.getDescription());
    }
}