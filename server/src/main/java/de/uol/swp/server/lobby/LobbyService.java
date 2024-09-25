package de.uol.swp.server.lobby;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.*;
import de.uol.swp.common.lobby.request.LobbyListRequest;
import de.uol.swp.common.lobby.response.LobbyListResponse;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;
import java.util.Optional;

/**
 * Handles the lobby requests send by the users
 *
 * @author Marco Grawunder
 * @since 2019-10-08
 */


@Singleton
public class LobbyService extends AbstractService {

    private final LobbyManagement lobbyManagement;
    private final AuthenticationService authenticationService;

    /**
     * Constructor
     *
     * @param lobbyManagement       The management class for creating, storing and deleting
     *                              lobbies
     * @param authenticationService the user management
     * @param eventBus              the server-wide EventBus
     * @since 2019-10-08
     */
    @Inject
    public LobbyService(
            LobbyManagement lobbyManagement, AuthenticationService authenticationService, EventBus eventBus
    ) {
        super(eventBus);
        this.lobbyManagement = lobbyManagement;
        this.authenticationService = authenticationService;
    }

    /**
     * Handles CreateLobbyRequests found on the EventBus
     *
     * If a CreateLobbyRequest is detected on the EventBus, this method is called.
     * It creates a new Lobby via the LobbyManagement using the parameters from the
     * request and sends a LobbyCreatedMessage to every connected user
     *
     * @param createLobbyRequest The CreateLobbyRequest found on the EventBus
     * @see de.uol.swp.server.lobby.LobbyManagement#createLobby(String, User)
     * @see de.uol.swp.common.lobby.message.LobbyCreatedMessage
     * @since 2019-10-08
     */
    @Subscribe
    public void onCreateLobbyRequest(CreateLobbyRequest createLobbyRequest) {
        lobbyManagement.createLobby(createLobbyRequest.getName(), createLobbyRequest.getOwner());
        sendToAll(new LobbyCreatedMessage(createLobbyRequest.getName(), (UserDTO) createLobbyRequest.getOwner()));
    }

    /**
     * Handles LobbyJoinUserRequests found on the EventBus
     *
     * If a LobbyJoinUserRequest is detected on the EventBus, this method is called.
     * It adds a user to a Lobby stored in the LobbyManagement and sends a UserJoinedLobbyMessage
     * to every user in the lobby.
     *
     * @param lobbyJoinUserRequest The LobbyJoinUserRequest found on the EventBus
     * @see de.uol.swp.common.lobby.ILobby
     * @see de.uol.swp.common.lobby.message.UserJoinedLobbyMessage
     * @since 2019-10-08
     */
    @Subscribe
    public void onLobbyJoinUserRequest(LobbyJoinUserRequest lobbyJoinUserRequest) {
        Optional<ILobby> lobby = lobbyManagement.getLobby(lobbyJoinUserRequest.getName());

        if (lobby.isPresent()) {
            lobby.get()
                 .joinUser(lobbyJoinUserRequest.getUser());
            sendToAllInLobby(
                    lobbyJoinUserRequest.getName(),
                    new UserJoinedLobbyMessage(lobbyJoinUserRequest.getName(), lobbyJoinUserRequest.getUser())
            );
        }
        // TODO: error handling not existing lobby
    }

    /**
     * Handles LobbyLeaveUserRequests found on the EventBus
     *
     * If a LobbyLeaveUserRequest is detected on the EventBus, this method is called.
     * It removes a user from a Lobby stored in the LobbyManagement and sends a
     * UserLeftLobbyMessage to every user in the lobby.
     *
     * @param lobbyLeaveUserRequest The LobbyJoinUserRequest found on the EventBus
     * @see de.uol.swp.common.lobby.ILobby
     * @see de.uol.swp.common.lobby.message.UserLeftLobbyMessage
     * @since 2019-10-08
     */
    @Subscribe
    public void onLobbyLeaveUserRequest(LobbyLeaveUserRequest lobbyLeaveUserRequest) {
        Optional<ILobby> lobby = lobbyManagement.getLobby(lobbyLeaveUserRequest.getName());

        if (lobby.isPresent()) {
            lobby.get()
                 .leaveUser(lobbyLeaveUserRequest.getUser());
            sendToAllInLobby(
                    lobbyLeaveUserRequest.getName(),
                    new UserLeftLobbyMessage(lobbyLeaveUserRequest.getName(), lobbyLeaveUserRequest.getUser())
            );
        }
        // TODO: error handling not existing lobby
    }

    /**
     * Prepares a given ServerMessage to be send to all players in the lobby and
     * posts it on the EventBus
     *
     * @param lobbyName Name of the lobby the players are in
     * @param message   the message to be send to the users
     * @see de.uol.swp.common.message.ServerMessage
     * @since 2019-10-08
     */
    public void sendToAllInLobby(String lobbyName, ServerMessage message) {
        Optional<ILobby> lobby = lobbyManagement.getLobby(lobbyName);

        if (lobby.isPresent()) {
            message.setReceiver(authenticationService.getSessions(lobby.get()
                                                                       .getUsers()));
            post(message);
        }

        // TODO: error handling not existing lobby
    }

    /**
     * Handles LobbyListRequests found on the EventBus
     * <p>
     * If a LobbyListRequest is detected on the EventBus, this method is called.
     * It retrieves the list of lobbies from the LobbyManagement and sends a
     * LobbyListResponse to the requester.
     *
     * @param request The LobbyListRequest found on the EventBus
     * @see de.uol.swp.common.lobby.response.LobbyListResponse
     * @since 2024-09-25
     */
    @Subscribe
    public void onLobbyListRequest(LobbyListRequest request) {
        Map<String, ILobby> lobbies = Map.of("1", new LobbyDTO("1", new UserDTO("test1", "test1"), "1", 4));
        ;
        LobbyListResponse response = new LobbyListResponse(lobbies);
        request.getSession()
               .ifPresent(response::setSession);
        request.getMessageContext()
               .ifPresent(response::setMessageContext);
        post(response);
    }

}
