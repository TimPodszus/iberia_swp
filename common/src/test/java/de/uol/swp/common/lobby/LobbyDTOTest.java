package de.uol.swp.common.lobby;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for the UserDTO
 *
 * @author Marco Grawunder
 * @since 2019-10-08
 */
class LobbyDTOTest {

    private static final User defaultUser = new UserDTO("marco", "marco", "marco@grawunder.de");
    private static final User notInLobbyUser = new UserDTO("no", "marco", "no@grawunder.de");

    private static final int NO_USERS = 10;
    private static final List<UserDTO> users;

    static {
        users = new ArrayList<>();
        for (int i = 0; i < NO_USERS; i++) {
            users.add(new UserDTO("marco" + i, "marco" + i, "marco" + i + "@grawunder.de"));
        }
        Collections.sort(users);
    }

    /**
     * This test check whether a lobby is created correctly
     *
     * If the variables are not set correctly the test fails
     *
     * @since 2019-10-08
     */
    @Test
    void createLobbyTest() {
        Lobby lobby = new LobbyDTO("test", defaultUser);

        assertEquals("test", lobby.getName());
        assertEquals(1, lobby.getUsers().size());
        assertEquals(defaultUser, lobby.getUsers().iterator().next());

    }

    /**
     * This test check whether a user can join a lobby
     *
     * The test fails if the size of the user list of the lobby does not get bigger
     * or a user who joined is not in the list.
     *
     * @since 2019-10-08
     */
    @Test
    void joinUserLobbyTest() {
        Lobby lobby = new LobbyDTO("test", defaultUser);

        lobby.joinUser(users.get(0));
        assertEquals(2,lobby.getUsers().size());
        assertTrue(lobby.getUsers().contains(users.get(0)));

        lobby.joinUser(users.get(0));
        assertEquals(2, lobby.getUsers().size());

        lobby.joinUser(users.get(1));
        assertEquals(3,lobby.getUsers().size());
        assertTrue(lobby.getUsers().contains(users.get(1)));
    }

    /**
     * This test check whether a user can leave a lobby
     *
     * The test fails if the size of the user list of the lobby does not get smaller
     * or the user who left is still in the list.
     *
     * @since 2019-10-08
     */
    @Test
    void leaveUserLobbyTest() {
        Lobby lobby = new LobbyDTO("test", defaultUser);
        users.forEach(lobby::joinUser);

        assertEquals(lobby.getUsers().size(), users.size() + 1);
        lobby.leaveUser(users.get(5));

        assertEquals(lobby.getUsers().size(), users.size() + 1 - 1);
        assertFalse(lobby.getUsers().contains(users.get(5)));
    }

    /**
     * Test to check if the owner can leave the Lobby correctly
     *
     * This test fails if the owner field is not updated if the owner leaves the
     * lobby or if he still is in the user list of the lobby.
     *
     * @since 2019-10-08
     */
    @Test
    void removeOwnerFromLobbyTest() {
        Lobby lobby = new LobbyDTO("test", defaultUser);
        users.forEach(lobby::joinUser);

        lobby.leaveUser(defaultUser);

        assertNotEquals(defaultUser, lobby.getOwner() );
        assertTrue(users.contains(lobby.getOwner()));

    }

    /**
     * This checks if the owner of a lobby can be updated and if he has have joined
     * the lobby
     *
     * This test fails if the owner cannot be updated or does not have to be joined
     *
     * @since 2019-10-08
     */
    @Test
    void updateOwnerTest() {
        Lobby lobby = new LobbyDTO("test", defaultUser);
        users.forEach(lobby::joinUser);

        lobby.updateOwner(users.get(6));
        assertEquals(lobby.getOwner(), users.get(6));

        assertThrows(IllegalArgumentException.class, () -> lobby.updateOwner(notInLobbyUser));
    }

    /**
     * This test check whether a lobby can be empty
     *
     * If the leaveUser function does not throw an Exception the test fails
     *
     * @since 2019-10-08
     */
    @Test
    void assureNonEmptyLobbyTest() {
        Lobby lobby = new LobbyDTO("test", defaultUser);

        assertThrows(IllegalArgumentException.class, () -> lobby.leaveUser(defaultUser));
    }


}
