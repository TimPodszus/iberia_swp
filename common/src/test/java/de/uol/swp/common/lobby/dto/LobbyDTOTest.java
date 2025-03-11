package de.uol.swp.common.lobby.dto;

import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test Class for the UserDTO
 *
 * @author Marco Grawunder
 * @since 2019-10-08
 */
class LobbyDTOTest {
    /**
     * Tests the equals and hashCode methods of LobbyDTO.
     */
    @Test
    void testEqualsAndHashCode() {
        IUserDTO user1 = new UserDTO("user1", "password1");
        IUserDTO user2 = new UserDTO("user2", "password2");
        IUserDTO owner = new UserDTO("owner", "password");

        List<IUserDTO> users1 = Arrays.asList(user1, user2);
        List<IUserDTO> users2 = Arrays.asList(user1, user2);

        LobbyDTO lobby1 = new LobbyDTO("lobby1", "Lobby Name", users1, owner, 3);
        LobbyDTO lobby2 = new LobbyDTO("lobby1", "Lobby Name", users2, owner, 3);
        LobbyDTO lobby3 = new LobbyDTO("lobby2", "Different Lobby", users1, owner, 3);
        LobbyDTO lobby4 = new LobbyDTO("lobby1", "Lobby Name", users1, new UserDTO("differentOwner", "password"), 3);

        assertEquals(lobby1, lobby1);
        assertEquals(lobby1, lobby2);

        assertNotEquals(lobby1, lobby3);
        assertNotEquals(lobby1, lobby4);
        assertNotEquals(null, lobby1);
        assertNotEquals(lobby1, new Object());

        assertEquals(lobby1.hashCode(), lobby2.hashCode());
        assertNotEquals(lobby1.hashCode(), lobby3.hashCode());
        assertNotEquals(lobby1.hashCode(), lobby4.hashCode());
    }
}
