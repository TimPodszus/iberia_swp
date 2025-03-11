package de.uol.swp.server.cards.data.eventcards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FavorableTimeEventCardTest {

    @Test
    void testConstructor() {
        FavorableTimeEventCard favorableTimeEventCard = new FavorableTimeEventCard(1);

        assertEquals(1, favorableTimeEventCard.getId());
        assertEquals("Günstige Zeit", favorableTimeEventCard.getTitle());
        assertEquals(
                "Zieh in der nächsten Infektionsphase nur 1 Infektionskarte, und zwar die unterste Karte des Infektionsstapels.",
                favorableTimeEventCard.getDescription()
        );
    }
}
