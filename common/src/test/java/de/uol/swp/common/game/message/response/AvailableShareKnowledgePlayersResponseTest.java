package de.uol.swp.common.game.message.response;

import de.uol.swp.common.player.IPlayerDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class AvailableShareKnowledgePlayersResponseTest {

    @Test
    public void testConstructorAndGetters() {
        String lobbyId = "testLobbyId";
        List<IPlayerDTO> players = List.of(mock(IPlayerDTO.class), mock(IPlayerDTO.class));

        AvailableShareKnowledgePlayersResponse response = new AvailableShareKnowledgePlayersResponse(lobbyId, players);

        assertEquals(lobbyId, response.getLobbyId());
        assertEquals(players, response.getPlayers());
    }
}