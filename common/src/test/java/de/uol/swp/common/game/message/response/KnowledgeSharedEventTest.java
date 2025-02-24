package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.dto.GameDTO;
import de.uol.swp.common.game.dto.IGameDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test class for KnowledgeSharedEvent.
 */
public class KnowledgeSharedEventTest {

    /**
     * Tests the KnowledgeSharedEvent constructor and its getters.
     */
    @Test
    public void testKnowledgeSharedEvent() {
        String lobbyId = "testLobby";
        boolean success = true;
        IGameDTO gameDTO = mock(GameDTO.class);

        KnowledgeSharedEvent event = new KnowledgeSharedEvent(lobbyId, success, gameDTO);

        assertEquals(lobbyId, event.getLobbyId());
        assertEquals(success, event.wasSuccessful());
        assertEquals(gameDTO, event.getGameDTO());
    }
}