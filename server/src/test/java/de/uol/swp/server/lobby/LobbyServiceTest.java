package de.uol.swp.server.lobby;


import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.request.CreateLobbyRequest;
import de.uol.swp.common.lobby.message.request.GetLobbyRequest;
import de.uol.swp.common.lobby.message.request.UpdateLobbyRequest;
import de.uol.swp.common.lobby.message.response.GetLobbyResponse;
import de.uol.swp.common.lobby.message.request.LobbyListRequest;
import de.uol.swp.common.lobby.message.response.LobbyListResponse;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

/**
 * Test class for the LobbyService.
 */
public class LobbyServiceTest extends EventBusBasedTest {
    /**
     * The first owner of the lobby.
     */
    static final UserDTO firstOwner = new UserDTO("Marco", "Marco");

    /**
     * The lobby instance used for testing.
     */
    static final ILobby lobby = new Lobby("testcode", "Test", List.of(firstOwner), firstOwner, 4);

    /**
     * Mocked instance of ILobbyManagement.
     */
    @Mock
    private ILobbyManagement lobbyManagement;

    /**
     * List of users for testing purposes.
     */
    List<User> userList = new ArrayList<>();

    /**
     * The LobbyService instance used for testing.
     */
    LobbyService lobbyService;

    /**
     * Handles LobbyListResponse events.
     *
     * @param e the LobbyListResponse event
     */
    @Subscribe
    public void onEvent(LobbyListResponse e) {
        handleEvent(e);
    }

    /**
     * Handles GetLobbyResponse events.
     *
     * @param e the GetLobbyResponse event
     */
    @Subscribe
    public void onEvent(GetLobbyResponse e) {
        handleEvent(e);
    }

    /**
     * Sets up the test environment before each test.
     *
     * @throws LobbyManagementException if an error occurs during setup
     */
    @BeforeEach
    public void setUp() throws LobbyManagementException {
        MockitoAnnotations.openMocks(this);
        userList.add(firstOwner);
        lobbyManagement = mock(LobbyManagement.class);
        when(lobbyManagement.createLobby("Test", firstOwner)).thenReturn(lobby);

        lobbyService = new LobbyService(lobbyManagement, null, getBus());
    }

    /**
     * Tests the creation of a lobby.
     *
     * @throws LobbyManagementException if an error occurs during lobby creation
     */
    @Test
    void createLobbyTest() throws LobbyManagementException {
        final CreateLobbyRequest request = new CreateLobbyRequest("Test", firstOwner);

        post(request);

        verify(lobbyManagement, atLeast(1)).createLobby("Test", firstOwner);
    }

    /**
     * Tests retrieving all lobbies.
     *
     * @throws InterruptedException     if the thread is interrupted
     * @throws LobbyManagementException if an error occurs during lobby retrieval
     */
    @Test
    void getAllLobbiesTest() throws InterruptedException, LobbyManagementException {
        when(lobbyManagement.getLobbies()).thenReturn(new ArrayList<>());

        postAndWait(new LobbyListRequest());

        assertInstanceOf(LobbyListResponse.class, event);
    }

    /**
     * Tests retrieving a specific lobby.
     *
     * @throws LobbyManagementException if an error occurs during lobby retrieval
     * @throws InterruptedException     if the thread is interrupted
     */
    @Test
    void getLobbyTest() throws LobbyManagementException, InterruptedException {
        when(lobbyManagement.getLobby("testcode")).thenReturn(Optional.of(lobby));

        postAndWait(new GetLobbyRequest("testcode", firstOwner));

        assertInstanceOf(GetLobbyResponse.class, event);
    }

    /**
     * Tests updating a lobby.
     *
     * @throws LobbyManagementException if an error occurs during lobby update
     */
    @Test
    void updateLobbyTest() throws LobbyManagementException {
        ILobbyDTO lobbyDTO = LobbyMapper.toDTO(lobby);

        when(lobbyManagement.updateLobby(LobbyMapper.toLobby(lobbyDTO))).thenReturn(lobby);

        post(new UpdateLobbyRequest(lobbyDTO, firstOwner));

        ArgumentCaptor<ILobby> captor = ArgumentCaptor.forClass(ILobby.class);
        verify(lobbyManagement, atLeast(1)).updateLobby(captor.capture());
        assertEquals(lobby, captor.getValue());
    }
}

