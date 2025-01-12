package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the LobbyUpdatedEvent.
 */
 class LobbyUpdatedEventTest {

    /**
     * A test user.
     */
    IUserDTO user = new UserDTO("TestUser", "testpassword");

    /**
     * A test lobby DTO.
     */
    ILobbyDTO lobbyDTO = new LobbyDTO("TestLobby", "TestOwner", List.of(user), user, 1);

    /**
     * Tests the LobbyUpdatedEvent constructor and getLobbyDTO method.
     */
    @Test
    void testLobbyUpdatedEvent() {
        LobbyUpdatedEvent lobbyUpdatedEvent = new LobbyUpdatedEvent(lobbyDTO);
        assertEquals(lobbyDTO, lobbyUpdatedEvent.getLobbyDTO());
    }
}
