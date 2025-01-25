package de.uol.swp.common.game.message.event;

import de.uol.swp.common.game.dto.IGameDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

public class InitialBoardUpdateEventTest {

    @Test
    public void testEqualsAndHashCode() {
        IGameDTO game1 = mock(IGameDTO.class);
        IGameDTO game2 = mock(IGameDTO.class);

       InitialBoardUpdateEvent event1 = new InitialBoardUpdateEvent("Lobby1", game1);
       InitialBoardUpdateEvent event2 = new InitialBoardUpdateEvent("Lobby1", game1);
       InitialBoardUpdateEvent event3 = new InitialBoardUpdateEvent("Lobby2", game2);

        assertEquals(event1, event2);
        assertNotEquals(event1, event3);

        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }
}
