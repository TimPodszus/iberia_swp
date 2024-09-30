package de.uol.swp.server.usermanagement.lobby;


import de.uol.swp.common.lobby.message.CreateLobbyRequest;
import de.uol.swp.common.lobby.message.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.message.LobbyLeaveUserRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.lobby.LobbyManagementException;

import de.uol.swp.server.lobby.store.LobbyStore;
import org.greenrobot.eventbus.EventBus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LobbyServiceTest extends EventBusBasedTest
{

    static final UserDTO firstOwner = new UserDTO("Marco", "Marco");
    static final UserDTO secondOwner = new UserDTO("Marco2", "Marco2");

    // Special version of event bus for testing
    final EventBus bus = EventBus.builder()
            .logNoSubscriberMessages(false)
            .sendNoSubscriberEvent(false)
            .throwSubscriberException(true)
            .build();





    private final LobbyManagement lobbyManagement = new LobbyManagement();

    List<User> userList = new ArrayList<>();
    @Mock
    LobbyStore lobbyStore = new LobbyStore();
    @BeforeEach
    public void setUp() throws LobbyManagementException {
        MockitoAnnotations.openMocks(this);
        userList.add(firstOwner);
        lobbyStore = mock(LobbyStore.class);
        lobbyManagement.setLobbyStore(lobbyStore);
    }

    @Test
    void createLobbyTest() throws SQLException {
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
    void lobbyJoinUserTest() throws LobbyManagementException, SQLException {
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
    void lobbyLeaveUserTest() throws LobbyManagementException, SQLException {
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

