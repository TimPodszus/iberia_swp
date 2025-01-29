package de.uol.swp.server.lobby;


import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.request.CreateLobbyRequest;
import de.uol.swp.common.lobby.message.request.GetLobbyRequest;
import de.uol.swp.common.lobby.message.request.LobbyListRequest;
import de.uol.swp.common.lobby.message.request.UpdateLobbyRequest;
import de.uol.swp.common.lobby.message.response.GetLobbyResponse;
import de.uol.swp.common.lobby.message.response.LobbyListResponse;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.lobby.store.LobbyStoreException;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserMapper;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    static final ILobby lobby = new Lobby("testcode",
            "Test",
            UserMapper.toUser(List.of(firstOwner)),
            UserMapper.toUser(firstOwner),
            4
    );

    /**
     * Mocked instance of ILobbyManagement.
     */
    @Mock
    private ILobbyManagement lobbyManagement;

    @Mock
    private AuthenticationService authenticationService;

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
     * <p>
     * This method initializes the mocked instances of ILobbyManagement and AuthenticationService,
     * creates a new instance of LobbyService, and sets the authenticationService field in the
     * AbstractService class to the mocked AuthenticationService instance. It also sets up the
     * behavior of the mocked lobbyManagement to return a predefined lobby when the createLobby
     * method is called.
     *
     * @throws LobbyStoreException    if an error occurs during lobby management
     * @throws NoSuchFieldException   if the authenticationService field is not found
     * @throws IllegalAccessException if the authenticationService field is not accessible
     */
    @BeforeEach
    public void setUp() throws LobbyStoreException, NoSuchFieldException, IllegalAccessException {
        lobbyManagement = mock(LobbyManagement.class);
        authenticationService = mock(AuthenticationService.class);
        lobbyService = new LobbyService(getBus(), lobbyManagement);

        Field authServiceField = AbstractService.class.getDeclaredField("authenticationService");
        authServiceField.setAccessible(true);
        authServiceField.set(lobbyService, authenticationService);

        when(lobbyManagement.createLobby("Test", UserMapper.toUser(firstOwner))).thenReturn(lobby);
    }

    /**
     * Tests the creation of a lobby.
     *
     * @throws LobbyStoreException if an error occurs during lobby creation
     */
    @Test
    void createLobbyTest() throws LobbyStoreException {
        final CreateLobbyRequest request = new CreateLobbyRequest("Test", firstOwner);

        post(request);

        verify(lobbyManagement, atLeast(1)).createLobby("Test", UserMapper.toUser(firstOwner));
    }

    /**
     * Tests retrieving all lobbies.
     *
     * @throws InterruptedException if the thread is interrupted
     * @throws LobbyStoreException  if an error occurs during lobby retrieval
     */
    @Test
    void getAllLobbiesTest() throws InterruptedException, LobbyStoreException {
        when(lobbyManagement.getLobbies()).thenReturn(new ArrayList<>());

        postAndWait(new LobbyListRequest());

        assertInstanceOf(LobbyListResponse.class, event);
    }

    /**
     * Tests retrieving a specific lobby.
     *
     * @throws LobbyStoreException  if an error occurs during lobby retrieval
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void getLobbyTest() throws LobbyStoreException, InterruptedException {
        when(lobbyManagement.getLobby("testcode")).thenReturn(lobby);

        postAndWait(new GetLobbyRequest("testcode", firstOwner));

        assertInstanceOf(GetLobbyResponse.class, event);
    }

    /**
     * Tests updating a lobby.
     *
     * @throws LobbyStoreException if an error occurs during lobby update
     */
    @Test
    void updateLobbyTest() throws LobbyStoreException {
        ILobbyDTO lobbyDTO = LobbyMapper.toDTO(lobby);

        when(lobbyManagement.updateLobby(LobbyMapper.toLobby(lobbyDTO))).thenReturn(lobby);

        post(new UpdateLobbyRequest(lobbyDTO, firstOwner));

        ArgumentCaptor<ILobby> captor = ArgumentCaptor.forClass(ILobby.class);
        verify(lobbyManagement, atLeast(1)).updateLobby(captor.capture());
        assertEquals(lobby, captor.getValue());
    }
}

