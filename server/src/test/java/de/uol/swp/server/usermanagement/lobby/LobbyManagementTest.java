package de.uol.swp.server.usermanagement.lobby;

import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.lobby.LobbyService;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("UnstableApiUsage")
public class LobbyManagementTest {

    static final UserDTO firstOwner = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");

    final EventBus bus = EventBus.getDefault();
    final UserManagement userManagement = new UserManagement(new MainMemoryBasedUserStore());
    final AuthenticationService authService = new AuthenticationService(bus, userManagement);
    final LobbyManagement lobbyManagement = new LobbyManagement();
    final LobbyService lobbyService = new LobbyService(lobbyManagement, authService, bus);

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
            assertEquals(lobbyManagement.getLobby("Test").get().getOwner(), firstOwner);
        }
    }

    @Test
    void dropLobbyTest() {
        LobbyManagement lobbyManagement = getDefaultManagement();

        lobbyManagement.dropLobby("Test");

        assertTrue(lobbyManagement.getLobby("Test").isEmpty());
    }

    @Test
    void getLobbyTest() {
        LobbyManagement lobbyManagement = getDefaultManagement();

        if(lobbyManagement.getLobby("Test").isPresent()){
            assertNotNull(lobbyManagement.getLobby("Test"));
            assertEquals(lobbyManagement.getLobby("Test").get().getOwner(), firstOwner);
        }
    }

}
