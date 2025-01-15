package de.uol.swp.common.game.message.event;

import de.uol.swp.common.game.dto.GameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class StartGameEventTest {
    @Mock
    private GameDTO gameDTO1;

    @Mock
    private GameDTO gameDTO2;

    private StartGameEvent event1;
    private StartGameEvent event2;
    private StartGameEvent event3;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        event1 = new StartGameEvent("Test", gameDTO1);
        event2 = new StartGameEvent("Test", gameDTO1);
        event3 = new StartGameEvent("Test", gameDTO2);
    }

    @Test
    void testEquals() {
        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
        assertNotEquals(null, event1);
        assertNotEquals("test", event1);
        assertEquals(event1, event1);
    }

    @Test
    void testHashCode() {
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    void getGameDTO() {
        assertEquals(gameDTO1, event1.getGameDTO());
    }
}
