package de.uol.swp.server.game;

import de.uol.swp.common.game.dto.GameDTO;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameMapperTest {
    @Test
    void testToDTO() {
        IGame game = new Game(1, "123");

        GameDTO gameDTO = (GameDTO) GameMapper.toDTO(game);

        assertNotNull(gameDTO);
        assertEquals("123", gameDTO.getGameId());
        assertFalse(gameDTO.getCities().isEmpty());
        assertFalse(gameDTO.getConnections().isEmpty());
        assertFalse(gameDTO.getRegions().isEmpty());
        assertFalse(gameDTO.getPlagues().isEmpty());
        assertFalse(gameDTO.getInfectionCardDrawPile().isEmpty());
        assertTrue(gameDTO.getInfectionCardDiscardPile().isEmpty());
        assertFalse(gameDTO.getPlayerCardDrawPile().isEmpty());
        assertTrue(gameDTO.getPlayerCardDiscardPile().isEmpty());
        assertTrue(gameDTO.getPlayers().isEmpty());
        assertEquals(1, gameDTO.getInfectionCounter());
        assertEquals(0, gameDTO.getEscalationStage());
        assertEquals(14, gameDTO.getWaterTreatmentsLeft());
        assertEquals(20, gameDTO.getTracksLeft());
        assertEquals(0, gameDTO.getCurrentPlayerIndex());
    }
}
