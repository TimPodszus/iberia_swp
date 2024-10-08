package de.uol.swp.client.lobby.overview;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for LobbyListItem.
 */
class LobbyListItemTest {

    /**
     * Tests the LobbyListItem constructor and its getters.
     */
    @Test
    void testLobbyListItem() {
        LobbyListItem lobbyListItem = new LobbyListItem("testcode", "testlobby", 1, 2);
        assertEquals("testcode", lobbyListItem.getLobbyCode());
        assertEquals("testlobby", lobbyListItem.getName());
        assertEquals("1/5", lobbyListItem.getPlayers());
        assertEquals("Mittel", lobbyListItem.getDifficulty());
    }
}
