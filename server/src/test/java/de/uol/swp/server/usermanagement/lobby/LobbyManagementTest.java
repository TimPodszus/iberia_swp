package de.uol.swp.server.usermanagement.lobby;

import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.DatabaseBasedUserStore;

import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class LobbyManagementTest {

    static final UserDTO firstOwner = new UserDTO("Marco", "Marco");

    final EventBus bus = EventBus.getDefault();
    final UserManagement userManagement = new UserManagement(new DatabaseBasedUserStore());
    final AuthenticationService authService = new AuthenticationService(bus, userManagement);
    final LobbyManagement lobbyManagement = new LobbyManagement();

    LobbyManagement getDefaultManagement() {
        LobbyManagement management = new LobbyManagement();
        management.createLobby("Test", firstOwner);
        return management;
    }

    @Test
    void createLobbyTest() {
        lobbyManagement.createLobby("Test", firstOwner);

        assertNotNull(lobbyManagement.getLobby("Test"));
        if(lobbyManagement.getLobby("Test").isPresent()){
            assertEquals(firstOwner,
                    lobbyManagement.getLobby("Test")
                                   .get()
                                   .getOwner());
        }
    }

    @Test
    void dropLobbyTest() {
        LobbyManagement localLobbyManagement = getDefaultManagement();

        localLobbyManagement.dropLobby("Test");

        assertTrue(localLobbyManagement.getLobby("Test").isEmpty());
    }

    @Test
    void getLobbyTest() {
        LobbyManagement localLobbyManagement = getDefaultManagement();

        if(localLobbyManagement.getLobby("Test").isPresent()){
            assertNotNull(localLobbyManagement.getLobby("Test"));
            assertEquals(firstOwner,
                    localLobbyManagement.getLobby("Test")
                                        .get()
                                        .getOwner());
        }
    }

}
