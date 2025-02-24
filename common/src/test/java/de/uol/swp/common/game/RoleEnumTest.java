package de.uol.swp.common.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleEnumTest {
    @Test
    void getColorCode() {
        assertEquals("#1AB429", RoleEnum.AGRICULTURAL_SCIENTIST.getColorCode());
    }

    @Test
    void getName() {
        assertEquals("Agrarwissenschaftler", RoleEnum.AGRICULTURAL_SCIENTIST.getName());
    }

    @Test
    void getDescription() {
        assertEquals(
                "Extraaktion um einen Wasseraufbereitungsmarker zu platzieren. Bei Wasseraufbereitung, darf ein zusätzlicher Marker platziert werden",
                RoleEnum.AGRICULTURAL_SCIENTIST.getDescription()
        );
    }

    @Test
    void values() {
        RoleEnum[] values = RoleEnum.values();
        assertNotNull(values);
        assertEquals(7, values.length);
    }

    @Test
    void valueOf() {
        assertEquals(RoleEnum.AGRICULTURAL_SCIENTIST, RoleEnum.valueOf("AGRICULTURAL_SCIENTIST"));

        assertThrows(IllegalArgumentException.class, () -> RoleEnum.valueOf("NON_EXISTING_ROLE"));
    }
}