package de.uol.swp.server.cards.data.eventcards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MigrationOverseasEventCardTest {

    @Test
    void testConstructor() {
        MigrationOverseasEventCard migrationOverseasEventCard = new MigrationOverseasEventCard(1);

        assertEquals(1, migrationOverseasEventCard.getId());
        assertEquals("Migration nach Übersee", migrationOverseasEventCard.getTitle());
        assertEquals("Der aktuelle Spieler darf bis zu zwei Seuchenwürfel entfernen.", migrationOverseasEventCard.getDescription());
    }
}