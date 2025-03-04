package de.uol.swp.server.infection.data;

import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InfectionTest {

    @Test
    void testDecreaseSeverity() {
        Infection infection = new Infection(10, PlagueName.TYPHUS);
        infection.decreaseSeverity(5);
        assertEquals(5, infection.getSeverity());
    }

    @Test
    void testDecreaseSeverityToZero() {
        Infection infection = new Infection(5, PlagueName.TYPHUS);
        infection.decreaseSeverity(5);
        assertEquals(0, infection.getSeverity());
    }

    @Test
    void testDecreaseSeverityBelowZero() {
        Infection infection = new Infection(5, PlagueName.TYPHUS);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> infection.decreaseSeverity(6));
        assertEquals("Severity cannot be negative", exception.getMessage());
    }
}