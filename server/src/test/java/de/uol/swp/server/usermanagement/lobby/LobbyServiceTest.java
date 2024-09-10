package de.uol.swp.server.usermanagement.lobby;

import de.uol.swp.common.lobby.message.CreateLobbyRequest;
import de.uol.swp.common.lobby.message.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.message.LobbyLeaveUserRequest;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.DatabaseBasedUserStore;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyServiceTest {

    static final UserDTO firstOwner = new UserDTO("Marco", "Marco");
    static final UserDTO secondOwner = new UserDTO("Marco2", "Marco2");

    // Special version of event bus for testing
    final EventBus bus = EventBus.builder()
            .logNoSubscriberMessages(false)
            .sendNoSubscriberEvent(false)
            .throwSubscriberException(true)
            .build();
    final UserManagement userManagement = new UserManagement(new DatabaseBasedUserStore());
    final AuthenticationService authService = new AuthenticationService(bus, userManagement);
    final LobbyManagement lobbyManagement = new LobbyManagement();

    @Test
    void createLobbyTest() {
        final CreateLobbyRequest request = new CreateLobbyRequest("Test", firstOwner);

        // The post will lead to a call of a LobbyService function
        bus.post(request);

        // Check if Lobby was created
        assertNotNull(lobbyManagement.getLobby("Test"));
        // Checks whether it is also the correct owner
        if (lobbyManagement.getLobby("Test").isPresent()) {
            assertEquals(firstOwner,
                    lobbyManagement.getLobby("Test")
                                   .get()
                                   .getOwner());
        }
    }

    @Test
    void createSecondLobbyWithSameName() {
        final CreateLobbyRequest request = new CreateLobbyRequest("Test", firstOwner);
        final CreateLobbyRequest request2 = new CreateLobbyRequest("Test", secondOwner);

        bus.post(request);

        // event bus throw exception
        Exception e = assertThrows(EventBusException.class,
                () -> bus.post(request2)
        );
        // Check if the nested exception is the right exception
        assertInstanceOf(IllegalArgumentException.class, e.getCause());

        // old lobby should be still in the LobbyManagement
        assertNotNull(lobbyManagement.getLobby("Test"));

        // old lobby should not be overwritten!
        if (lobbyManagement.getLobby("Test").isPresent()) {
            assertNotEquals(secondOwner,
                    lobbyManagement.getLobby("Test")
                                   .get()
                                   .getOwner());
        }
    }

    @Test
    void lobbyJoinUserTest() {
        // Create the lobby
        lobbyManagement.createLobby("Test", firstOwner);

        final LobbyJoinUserRequest request = new LobbyJoinUserRequest("Test", secondOwner);

        // The post will lead to a call of a LobbyService function
        bus.post(request);

        // Check if the user is listed in the lobby
        if (lobbyManagement.getLobby("Test").isPresent()) {
            assertTrue(lobbyManagement.getLobby("Test").get().getUsers().contains(firstOwner));
            assertTrue(lobbyManagement.getLobby("Test").get().getUsers().contains(secondOwner));
        }
    }

    @Test
    void lobbyLeaveUserTest() {
        // Create the lobby
        lobbyManagement.createLobby("Test", firstOwner);
        // Join User
        if (lobbyManagement.getLobby("Test").isPresent()) {
            lobbyManagement.getLobby("Test").get().joinUser(secondOwner);
        }

        final LobbyLeaveUserRequest request = new LobbyLeaveUserRequest("Test", secondOwner);

        // The post will lead to a call of a LobbyService function
        bus.post(request);

        // Check if the user is no longer listed in the lobby
        if (lobbyManagement.getLobby("Test").isPresent()) {
            assertFalse(lobbyManagement.getLobby("Test").get().getUsers().contains(secondOwner));
        }
    }

}
