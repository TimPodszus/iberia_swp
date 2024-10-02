package de.uol.swp.server.lobby;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.request.LobbyListRequest;
import de.uol.swp.common.lobby.response.LobbyListResponse;
import de.uol.swp.common.lobby.message.request.*;
import de.uol.swp.common.lobby.message.response.GetLobbyResponse;
import de.uol.swp.common.lobby.message.response.LobbyCreatedMessage;
import de.uol.swp.common.lobby.message.response.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.response.UserLeftLobbyMessage;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Handles the lobby requests send by the users
 *
 * @author Marco Grawunder
 * @since 2019-10-08
 */


@Singleton
public class LobbyService extends AbstractService {

    private final ILobbyManagement lobbyManagement;
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
            ILobbyManagement lobbyManagement, AuthenticationService authenticationService, EventBus eventBus
    ) {
        super(eventBus);
        this.lobbyManagement = lobbyManagement;
        this.authenticationService = authenticationService;
    }

    /**
     * Handles CreateLobbyRequests found on the EventBus
     * If a CreateLobbyRequest is detected on the EventBus, this method is called.
     * It creates a new Lobby via the LobbyManagement using the parameters from the
     * request and sends a LobbyCreatedMessage to every connected user
     *
     * @param createLobbyRequest The CreateLobbyRequest found on the EventBus
     * @see LobbyManagement#createLobby(String, User)
     * @see LobbyCreatedMessage
     * @since 2019-10-08
     */
    @Subscribe
    public void onCreateLobbyRequest(CreateLobbyRequest createLobbyRequest) throws LobbyManagementException {
        ILobby createdLobby = lobbyManagement.createLobby(createLobbyRequest.getName(), createLobbyRequest.getOwner());
        sendToAll(new LobbyCreatedMessage(createdLobby.getName(), (UserDTO) createLobbyRequest.getOwner()));
    }

    /**
     * Handles LobbyJoinUserRequests found on the EventBus
     * If a LobbyJoinUserRequest is detected on the EventBus, this method is called.
     * It adds a user to a Lobby stored in the LobbyManagement and sends a UserJoinedLobbyMessage
     * to every user in the lobby.
     *
     * @param lobbyJoinUserRequest The LobbyJoinUserRequest found on the EventBus
     * @see ILobby
     * @see UserJoinedLobbyMessage
     * @since 2019-10-08
     */
    @Subscribe
    public void onLobbyJoinUserRequest(LobbyJoinUserRequest lobbyJoinUserRequest) throws LobbyManagementException {
        Optional<ILobby> lobby = lobbyManagement.getLobby(lobbyJoinUserRequest.getLobbyCode());

        if (lobby.isPresent()) {
            lobby.get()
                 .joinUser(lobbyJoinUserRequest.getUser());
            sendToAllInLobby(
                    lobbyJoinUserRequest.getLobbyCode(),
                    new UserJoinedLobbyMessage(lobbyJoinUserRequest.getLobbyCode(), lobbyJoinUserRequest.getUser())
            );
        }
        // TODO: error handling not existing lobby
    }

    /**
     * Handles LobbyLeaveUserRequests found on the EventBus
     * If a LobbyLeaveUserRequest is detected on the EventBus, this method is called.
     * It removes a user from a Lobby stored in the LobbyManagement and sends a
     * UserLeftLobbyMessage to every user in the lobby.
     *
     * @param lobbyLeaveUserRequest The LobbyJoinUserRequest found on the EventBus
     * @see ILobby
     * @see UserLeftLobbyMessage
     * @since 2019-10-08
     */
    @Subscribe
    public void onLobbyLeaveUserRequest(LobbyLeaveUserRequest lobbyLeaveUserRequest) throws LobbyManagementException {
        Optional<ILobby> lobby = lobbyManagement.getLobby(lobbyLeaveUserRequest.getLobbyCode());

        if (lobby.isPresent()) {
            lobby.get()
                 .leaveUser(lobbyLeaveUserRequest.getUser());
            sendToAllInLobby(
                    lobbyLeaveUserRequest.getLobbyCode(),
                    new UserLeftLobbyMessage(lobbyLeaveUserRequest.getLobbyCode(), lobbyLeaveUserRequest.getUser())
            );
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
    public void onLobbyListRequest(LobbyListRequest request) throws LobbyManagementException {
        List<ILobbyDTO> lobbies = lobbyManagement.getLobbies()
                                                 .stream()
                                                 .map(LobbyMapper::toDTO)
                                                 .toList();
        LobbyListResponse response = new LobbyListResponse(lobbies);
        request.getSession()
               .ifPresent(response::setSession);
        request.getMessageContext()
               .ifPresent(response::setMessageContext);
        post(response);
    }

    /**
     * Handles GetLobbyRequests found on the EventBus.
     * If a GetLobbyRequest is detected on the EventBus, this method is called.
     * It retrieves the lobby information from the LobbyManagement and sends a GetLobbyResponse.
     *
     * @param request The GetLobbyRequest found on the EventBus
     * @see ILobby
     * @see GetLobbyResponse
     * @since 2019-10-08
     */
    @Subscribe
    public void onGetLobbyRequest(GetLobbyRequest request) throws LobbyManagementException {
        Optional<ILobby> lobby = lobbyManagement.getLobby(request.getLobbyCode());
        if (lobby.isPresent()) {
            ILobbyDTO lobbyDTO = LobbyMapper.toDTO(lobby.get());
            GetLobbyResponse response = new GetLobbyResponse(lobbyDTO);
            request.getMessageContext()
                   .ifPresent(response::setMessageContext);
            request.getSession()
                   .ifPresent(response::setSession);
            post(response);
        }
    }

    /**
     * Handles UpdateLobbyRequests found on the EventBus.
     * If an UpdateLobbyRequest is detected on the EventBus, this method is called.
     * It updates the lobby information in the LobbyManagement using the parameters from the request.
     *
     * @param request The UpdateLobbyRequest found on the EventBus
     * @see ILobby
     * @see ILobbyDTO
     * @since 2019-10-08
     */
    @Subscribe
    public void onUpdateLobbyRequest(UpdateLobbyRequest request) throws LobbyManagementException {
        ILobbyDTO lobbyDTO = request.getLobbyDTO();
        ILobby lobby = LobbyMapper.toLobby(lobbyDTO);
        lobbyManagement.updateLobby(lobby);
    }

    /**
     * Prepares a given ServerMessage to be sent to all players in the lobby and
     * posts it on the EventBus
     *
     * @param lobbyName Name of the lobby the players are in
     * @param message   the message to be sent to the users
     * @see ServerMessage
     * @since 2019-10-08
     */
    public void sendToAllInLobby(String lobbyName, ServerMessage message) throws LobbyManagementException {
        Optional<ILobby> lobby = lobbyManagement.getLobby(lobbyName);

        if (lobby.isPresent()) {
            message.setReceiver(authenticationService.getSessions(new HashSet<>(lobby.get()
                                                                                     .getUsers())));
            post(message);
        }

        // TODO: error handling not existing lobby
    }
}
