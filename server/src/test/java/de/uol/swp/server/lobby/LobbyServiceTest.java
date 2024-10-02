package de.uol.swp.server.lobby;


import de.uol.swp.common.lobby.message.request.CreateLobbyRequest;
import de.uol.swp.common.lobby.request.LobbyListRequest;
import de.uol.swp.common.lobby.response.LobbyListResponse;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

public class LobbyServiceTest extends EventBusBasedTest {
    static final UserDTO firstOwner = new UserDTO("Marco", "Marco");
    static final ILobby lobby = new Lobby("Test", "testcode", List.of(firstOwner), firstOwner, 4);

    @Mock
    private ILobbyManagement lobbyManagement;
    List<User> userList = new ArrayList<>();
    LobbyService lobbyService;

    @Subscribe
    public void onEvent(LobbyListResponse e) {
        handleEvent(e);
    }

    @BeforeEach
    public void setUp() throws LobbyManagementException {
        MockitoAnnotations.openMocks(this);
        userList.add(firstOwner);
        lobbyManagement = mock(LobbyManagement.class);
        when(lobbyManagement.createLobby("Test", firstOwner)).thenReturn(lobby);

        lobbyService = new LobbyService(lobbyManagement, null, getBus());
    }

    @Test
    void createLobbyTest() throws LobbyManagementException {
        final CreateLobbyRequest request = new CreateLobbyRequest("Test", firstOwner);
        // The post will lead to a call of a LobbyService function
        post(request);

        verify(lobbyManagement, atLeast(1)).createLobby("Test", firstOwner);
    }

    @Test
    void getAllLobbiesTest() throws InterruptedException, LobbyManagementException {
        when(lobbyManagement.getLobbies()).thenReturn(new ArrayList<>());

        postAndWait(new LobbyListRequest());

        assertInstanceOf(LobbyListResponse.class, event);
    }
}

