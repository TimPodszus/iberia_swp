package de.uol.swp.server.lobby;


import de.uol.swp.common.lobby.message.request.CreateLobbyRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import de.uol.swp.server.lobby.store.LobbyStore;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class LobbyServiceTest extends EventBusBasedTest {

    static final UserDTO firstOwner = new UserDTO("Marco", "Marco");

    // Special version of event bus for testing
    final EventBus bus = EventBus.builder()
                                 .logNoSubscriberMessages(false)
                                 .sendNoSubscriberEvent(false)
                                 .throwSubscriberException(true)
                                 .build();

    private LobbyManagement lobbyManagement;
    List<User> userList = new ArrayList<>();
    @Mock
    LobbyStore lobbyStore;

    @BeforeEach
    public void setUp() throws LobbyManagementException {
        MockitoAnnotations.openMocks(this);
        userList.add(firstOwner);
        lobbyStore = mock(LobbyStore.class);
        lobbyManagement = new LobbyManagement(lobbyStore);
    }

    @Test
    void createLobbyTest() throws LobbyManagementException {
        final CreateLobbyRequest request = new CreateLobbyRequest("Test", firstOwner);

        // The post will lead to a call of a LobbyService function
        bus.post(request);

        // Check if Lobby was created
        assertNotNull(lobbyManagement.getLobby("Test"));
        // Checks whether it is also the correct owner
        if (lobbyManagement.getLobby("Test")
                           .isPresent()) {
            assertEquals(
                    firstOwner,
                    lobbyManagement.getLobby("Test")
                                   .get()
                                   .getOwner()
            );
        }
    }
}

