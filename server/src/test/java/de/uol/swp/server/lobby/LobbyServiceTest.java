package de.uol.swp.server.lobby;


import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.request.*;
import de.uol.swp.common.lobby.message.response.GetLobbyResponse;
import de.uol.swp.common.lobby.message.response.LobbyListResponse;
import de.uol.swp.common.lobby.message.response.LobbyUpdatedEvent;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.store.LobbyStoreException;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

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
    @InjectMocks
    LobbyService lobbyService = new LobbyService(super.getBus(), lobbyManagement);

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
     * Handles LobbyUpdatedEvent events.
     *
     * @param e the LobbyUpdatedEvent event
     */
    @Subscribe
    public void onLobbyUpdatedEvent(LobbyUpdatedEvent e) {
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
        MockitoAnnotations.openMocks(this);
        when(lobbyManagement.createLobby(UserMapper.toUser(firstOwner))).thenReturn(lobby);
    }

    /**
     * Tests the creation of a lobby.
     *
     * @throws LobbyStoreException if an error occurs during lobby creation
     */
    @Test
    void createLobbyTest() throws LobbyStoreException {
        IUser user = UserMapper.toUser(firstOwner);
        Session session = UUIDSession.create(user);
        final CreateLobbyRequest request = new CreateLobbyRequest();
        request.setSession(session);

        post(request);

        verify(lobbyManagement, atLeast(1)).createLobby(UserMapper.toUser(firstOwner));
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
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void getLobbyTest() throws InterruptedException {
        when(lobbyManagement.getLobby("testcode")).thenReturn(lobby);
        GetLobbyRequest request = new GetLobbyRequest("testcode");
        Session session = UUIDSession.create(UserMapper.toUser(firstOwner));
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(GetLobbyResponse.class, event);
    }

    /**
     * Tests updating a lobby.
     *
     * @throws LobbyStoreException if an error occurs during lobby update
     */
    @Test
    void updateLobbyTest() throws LobbyStoreException, InterruptedException {
        ILobbyDTO lobbyDTO = LobbyMapper.toDTO(lobby);
        when(lobbyManagement.updateLobby(LobbyMapper.toLobby(lobbyDTO))).thenReturn(lobby);
        UpdateLobbyRequest request = new UpdateLobbyRequest(lobbyDTO);
        Session session = UUIDSession.create(UserMapper.toUser(firstOwner));
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(LobbyUpdatedEvent.class, event);
        verify(lobbyManagement, atLeast(1)).updateLobby(lobby);
    }

    /**
     * Tests removing a user from a lobby.
     *
     * @throws LobbyStoreException  if an error occurs during user removal
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void testRemoveUserFromLobby() throws LobbyStoreException, InterruptedException {
        when(lobbyManagement.removeUser("testcode", "Marco")).thenReturn(lobby);
        RemoveUserFromLobbyRequest request = new RemoveUserFromLobbyRequest("testcode", "Marco");
        Session session = UUIDSession.create(UserMapper.toUser(firstOwner));
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(LobbyUpdatedEvent.class, event);
        verify(lobbyManagement, atLeast(1)).removeUser("testcode", "Marco");
    }
}

