package de.uol.swp.server.lobby;


import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.request.*;
import de.uol.swp.common.lobby.message.response.GetLobbyResponse;
import de.uol.swp.common.lobby.message.response.LobbyListResponse;
import de.uol.swp.common.lobby.message.response.LobbyUpdatedEvent;
import de.uol.swp.common.lobby.message.response.UserJoinedLobbyMessage;
import de.uol.swp.common.message.response.ExceptionMessage;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.exceptions.LobbyNotFoundException;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.UserMapper;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
     * Handles UserJoinedLobbyMessage events.
     *
     * @param e the UserJoinedLobbyMessage event
     */
    @Subscribe
    public void onUserJoinedLobbyMessage(UserJoinedLobbyMessage e) {
        handleEvent(e);
    }

    /**
     * Handles ExceptionMessage events.
     *
     * @param e the ExceptionMessage event
     */
    @Subscribe
    public void onExceptionMessage(ExceptionMessage e) {
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
     * @throws NoSuchFieldException   if the authenticationService field is not found
     * @throws IllegalAccessException if the authenticationService field is not accessible
     */
    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        when(lobbyManagement.createLobby(UserMapper.toUser(firstOwner))).thenReturn(lobby);
    }

    /**
     * Tests the creation of a lobby.
     */
    @Test
    void createLobbyTest() {
        IUser user = UserMapper.toUser(firstOwner);
        Session session = UUIDSession.create(user);
        final CreateLobbyRequest request = new CreateLobbyRequest();
        request.setSession(session);

        post(request);

        verify(lobbyManagement, atLeast(1)).createLobby(UserMapper.toUser(firstOwner));
    }

    /**
     * Tests the creation of a lobby with an invalid session.
     */
    @Test
    void testCreateLobby_InvalidSession() {
        final CreateLobbyRequest request = new CreateLobbyRequest();
        assertThrows(SessionNotFoundException.class, () -> lobbyService.onCreateLobbyRequest(request));
    }

    /**
     * Tests retrieving all lobbies.
     *
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void getAllLobbiesTest() throws InterruptedException {
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
     * @throws LobbyNotFoundException if an error occurs during lobby update
     */
    @Test
    void updateLobbyTest() throws InterruptedException, LobbyNotFoundException {
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
     * Tests updating a lobby.
     *
     * @throws LobbyNotFoundException if an error occurs during lobby update
     */
    @Test
    void testUpdateLobby_LobbyNotFound() throws InterruptedException, LobbyNotFoundException {
        ILobbyDTO lobbyDTO = LobbyMapper.toDTO(lobby);
        when(lobbyManagement.updateLobby(LobbyMapper.toLobby(lobbyDTO))).thenThrow(LobbyNotFoundException.class);
        UpdateLobbyRequest request = new UpdateLobbyRequest(lobbyDTO);
        Session session = UUIDSession.create(UserMapper.toUser(firstOwner));
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(ExceptionMessage.class, event);
    }

    /**
     * Tests removing a user from a lobby.
     *
     * @throws LobbyNotFoundException  if an error occurs during user removal
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void testRemoveUserFromLobby() throws LobbyNotFoundException, InterruptedException {
        IUser userToRemove = new User("RemoveMe", "RemoveMe");
        Session userToRemoveSession = UUIDSession.create(userToRemove);
        when(authenticationService.getSession(userToRemove)).thenReturn(Optional.of(userToRemoveSession));
        lobby.addUser(userToRemove);

        when(lobbyManagement.getLobby("testcode")).thenReturn(lobby);
        when(lobbyManagement.removeUser("testcode", "RemoveMe")).thenReturn(lobby);
        RemoveUserFromLobbyRequest request = new RemoveUserFromLobbyRequest("testcode", "RemoveMe");
        Session session = UUIDSession.create(UserMapper.toUser(firstOwner));
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(LobbyUpdatedEvent.class, event);
        verify(lobbyManagement, atLeast(1)).removeUser("testcode", "RemoveMe");
    }

    /**
     * Tests removing a user from a lobby when the lobby is not found.
     *
     * @throws LobbyNotFoundException if the lobby is not found
     * @throws InterruptedException   if the thread is interrupted
     */
    @Test
    void testRemoveUserFromLobby_LobbyNotFound() throws LobbyNotFoundException, InterruptedException {
        when(lobbyManagement.getLobby("testcode")).thenReturn(lobby);
        when(lobbyManagement.removeUser("testcode", "RemoveMe")).thenThrow(LobbyNotFoundException.class);
        RemoveUserFromLobbyRequest request = new RemoveUserFromLobbyRequest("testcode", "RemoveMe");

        postAndWait(request);

        assertInstanceOf(ExceptionMessage.class, event);
        verify(lobbyManagement, never()).getLobby(anyString());
    }

    /**
     * Tests removing a user from a lobby when the session is not found.
     *
     * @throws LobbyNotFoundException if the lobby is not found
     */
    @Test
    void testRemoveUserFromLobby_SessionNotFound() throws LobbyNotFoundException {
        IUser userToRemove = new User("RemoveMe", "RemoveMe");
        lobby.addUser(userToRemove);
        when(authenticationService.getSession(userToRemove)).thenReturn(Optional.empty());

        when(lobbyManagement.getLobby("testcode")).thenReturn(lobby);
        when(lobbyManagement.removeUser("testcode", "RemoveMe")).thenReturn(lobby);
        RemoveUserFromLobbyRequest request = new RemoveUserFromLobbyRequest("testcode", "RemoveMe");
        Session session = UUIDSession.create(UserMapper.toUser(firstOwner));
        request.setSession(session);

        assertThrows(SessionNotFoundException.class, () -> lobbyService.onRemoveUserFromLobbyRequest(request));
    }

    /**
     * Tests leaving a lobby.
     *
     * @throws InterruptedException if the eventbus is interrupted
     */
    @Test
    void testLeaveLobby() throws InterruptedException {
        when(lobbyManagement.getLobby("testcode")).thenReturn(lobby);
        IUser user = UserMapper.toUser(firstOwner);
        LeaveLobbyRequest request = new LeaveLobbyRequest("testcode");
        Session session = UUIDSession.create(user);
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(LobbyUpdatedEvent.class, event);
        verify(lobbyManagement, atLeast(1)).leaveLobby("testcode", user);
    }

    /**
     * Tests joining a lobby.
     */
    @Test
    void testLeaveLobby_InvalidSession() {
        final LeaveLobbyRequest request = new LeaveLobbyRequest("testcode");
        assertThrows(SessionNotFoundException.class, () -> lobbyService.onLobbyLeaveUserRequest(request));
    }

    /**
     * Tests joining a lobby.
     *
     * @throws LobbyNotFoundException  if an error occurs during lobby joining
     * @throws InterruptedException if the eventbus is interrupted
     */
    @Test
    void testJoinLobby() throws LobbyNotFoundException, InterruptedException {
        when(lobbyManagement.getLobby("testcode")).thenReturn(lobby);
        IUser user = UserMapper.toUser(firstOwner);
        JoinLobbyRequest request = new JoinLobbyRequest("testcode");
        Session session = UUIDSession.create(user);
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(UserJoinedLobbyMessage.class, event);
        verify(lobbyManagement, atLeastOnce()).joinLobby("testcode", user);
    }

    /**
     * Tests joining a lobby when the lobby is not found.
     *
     * @throws LobbyNotFoundException if the lobby is not found
     * @throws InterruptedException   if the eventbus is interrupted
     */
    @Test
    void testJoinLobby_LobbyNotFound() throws InterruptedException, LobbyNotFoundException {
        IUser user = UserMapper.toUser(firstOwner);
        JoinLobbyRequest request = new JoinLobbyRequest("testcode");
        Session session = UUIDSession.create(user);
        request.setSession(session);

        doThrow(new LobbyNotFoundException("Lobby not found")).when(lobbyManagement)
                                                              .joinLobby("testcode", user);

        postAndWait(request);

        assertInstanceOf(ExceptionMessage.class, event);
        verify(lobbyManagement, never()).getLobby(anyString());
    }

    /**
     * Tests joining a lobby with an invalid session.
     */
    @Test
    void testJoinLobby_InvalidSession() {
        final JoinLobbyRequest request = new JoinLobbyRequest("testcode");
        assertThrows(SessionNotFoundException.class, () -> lobbyService.onLobbyJoinUserRequest(request));
    }
}

