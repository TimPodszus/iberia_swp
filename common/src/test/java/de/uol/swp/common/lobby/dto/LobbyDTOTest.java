package de.uol.swp.common.lobby.dto;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Class for the UserDTO
 *
 * @author Marco Grawunder
 * @since 2019-10-08
 */
class LobbyDTOTest {
    /**
     * This test check whether a lobby is created correctly
     * If the variables are not set correctly the test fails
     *
     * @since 2019-10-08
     */
    @Test
    void createLobbyTest() {
        User defaultUser = new UserDTO("marco", "marco");
        ILobbyDTO lobbyDTO = new LobbyDTO("testcode", "test", List.of(defaultUser), defaultUser, 4);
        assertEquals("test", lobbyDTO.getName());
        assertEquals(
                1,
                lobbyDTO.getUsers()
                        .size()
        );
        assertEquals(
                defaultUser,
                lobbyDTO.getUsers()
                        .iterator()
                        .next()
        );

    }


}
