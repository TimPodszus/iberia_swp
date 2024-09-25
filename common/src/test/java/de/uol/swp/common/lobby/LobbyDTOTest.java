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

    private static final User defaultUser = new UserDTO("marco", "marco");
    private static final User notInLobbyUser = new UserDTO("no", "marco");

    private static final int NO_USERS = 10;
    private static final List<UserDTO> users;

    static {
        users = new ArrayList<>();
        for (int i = 0; i < NO_USERS; i++) {
            users.add(new UserDTO("marco" + i, "marco" + i));
        }
        Collections.sort(users);
    }

    /**
     * This test check whether a lobby is created correctly
     * If the variables are not set correctly the test fails
     *
     * @since 2019-10-08
     */
    @Test
    void createLobbyTest() {
        // not implemented

    }

    /**
     * This test check whether a user can join a lobby
     * The test fails if the size of the user list of the lobby does not get bigger
     * or a user who joined is not in the list.
     *
     * @since 2019-10-08
     */
    @Test
    void joinUserLobbyTest() {
        // not implemented
    }

    /**
     * This test check whether a user can leave a lobby
     * The test fails if the size of the user list of the lobby does not get smaller
     * or the user who left is still in the list.
     *
     * @since 2019-10-08
     */
    @Test
    void leaveUserLobbyTest() {
        // not implemented
    }

    /**
     * Test to check if the owner can leave the Lobby correctly
     * This test fails if the owner field is not updated if the owner leaves the
     * lobby or if he still is in the user list of the lobby.
     *
     * @since 2019-10-08
     */
    @Test
    void removeOwnerFromLobbyTest() {
        // not implemented

    }

    /**
     * This checks if the owner of a lobby can be updated and if he has have joined
     * the lobby
     * This test fails if the owner cannot be updated or does not have to be joined
     *
     * @since 2019-10-08
     */
    @Test
    void updateOwnerTest() {
        // not implemented
    }

    /**
     * This test check whether a lobby can be empty
     * If the leaveUser function does not throw an Exception the test fails
     *
     * @since 2019-10-08
     */
    @Test
    void assureNonEmptyLobbyTest() {
        // not implemented
    }


}
