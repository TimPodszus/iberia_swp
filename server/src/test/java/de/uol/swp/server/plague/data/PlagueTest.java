package de.uol.swp.server.plague.data;

import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlagueTest {

    @Test
    void testIncreaseCubes() {
        Plague plague = new Plague(PlagueName.CHOLERA, 10, false);
        plague.increaseCubes(5);
        assertEquals(15, plague.getCubesRemaining());
    }

    @Test
    void testIncreaseCubesToMax() {
        Plague plague = new Plague(PlagueName.CHOLERA, 20, false);
        plague.increaseCubes(4);
        assertEquals(24, plague.getCubesRemaining());
    }

    @Test
    void testIncreaseCubesExceedMax() {
        Plague plague = new Plague(PlagueName.CHOLERA, 20, false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> plague.increaseCubes(5));
        assertEquals("Amount of cubes cannot exceed 24", exception.getMessage());
    }
}