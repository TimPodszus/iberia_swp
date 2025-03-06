package de.uol.swp.server.lobby;

import com.google.inject.Inject;
import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.event.RemovedFromLobbyEvent;
import de.uol.swp.common.lobby.message.request.*;
import de.uol.swp.common.lobby.message.response.*;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.store.LobbyStoreException;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Handles the lobby requests send by the users
 *
 * @author Marco Grawunder
 * @since 2019-10-08
 */
public class LobbyService extends AbstractService {
    public static final Logger LOG = LogManager.getLogger(LobbyService.class);
    private static final String SESSION_INVALID_OR_MISSING = "Session is missing or invalid";

    /**
     * The LobbyManagement instance used for managing lobbies.
     * This field is injected by the dependency injection framework.
     * This needs to be private to allow mocking in tests.
     */
    private ILobbyManagement lobbyManagement;

    /**
     * Constructs a new LobbyService.
     * <p>
     * This constructor is used to create an instance of LobbyService and
     * initializes it with the provided EventBus.
     *
     * @param eventBus the EventBus to be used by this service
     */
    @Inject
    public LobbyService(EventBus eventBus, ILobbyManagement lobbyManagement) {
        super(eventBus);
        this.lobbyManagement = lobbyManagement;
    }

    /**
     * Handles CreateLobbyRequests found on the EventBus
     * If a CreateLobbyRequest is detected on the EventBus, this method is called.
     * It creates a new Lobby via the LobbyManagement using the parameters from the
     * request and sends a LobbyCreatedResponse to every connected user
     *
     * @param createLobbyRequest The CreateLobbyRequest found on the EventBus
     * @see LobbyCreatedResponse
     * @since 2019-10-08
     */
    @Subscribe
    public void onCreateLobbyRequest(CreateLobbyRequest createLobbyRequest) throws LobbyStoreException {
        LOG.debug("Received create lobby request");
        Session session = createLobbyRequest.getSession()
                                            .orElseThrow(() -> {
                                                LOG.error(SESSION_INVALID_OR_MISSING);
                                                return new IllegalArgumentException(SESSION_INVALID_OR_MISSING);
                                            });
        ILobby createdLobby = lobbyManagement.createLobby(UserMapper.toUser(session.getUser()));
        LOG.info("[LobbyId: {}] Created lobby", createdLobby.getLobbyId());
        LobbyCreatedResponse response = new LobbyCreatedResponse(LobbyMapper.toDTO(createdLobby));
        response.setSession(session);
        createLobbyRequest.getMessageContext()
                          .ifPresent(response::setMessageContext);
        post(response);
        LOG.debug("[LobbyId: {}] Sent lobby created response", createdLobby.getLobbyId());
        sendServerMessageEvent(
                createdLobby.getLobbyId(),
                "Die Lobby mit dem Namen " + createdLobby.getName() + " wurde erfolgreich durch den Spieler " + createdLobby.getOwner().getUsername() + " erstellt. Viel Spaß!"
        );
    }

    /**
     * Handles LobbyJoinUserRequests found on the EventBus
     * If a LobbyJoinUserRequest is detected on the EventBus, this method is called.
     * It adds a user to a Lobby stored in the LobbyManagement and sends a UserJoinedLobbyMessage
     * to every user in the lobby.
     *
     * @param request The LobbyJoinUserRequest found on the EventBus
     * @see ILobby
     * @see UserJoinedLobbyMessage
     * @since 2019-10-08
     */
    @Subscribe
    public void onLobbyJoinUserRequest(JoinLobbyRequest request) throws LobbyStoreException {
        LOG.debug("[LobbyId: {}] Received join lobby request", request.getLobbyId());
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        IUserDTO user = request.getSession()
                               .orElseThrow(() -> {
                                   LOG.error(SESSION_INVALID_OR_MISSING);
                                   return new IllegalArgumentException(SESSION_INVALID_OR_MISSING);
                               })
                               .getUser();
        lobbyManagement.joinLobby(request.getLobbyId(), UserMapper.toUser(user));
        LOG.info("[LobbyId: {}] User joined lobby", request.getLobbyId());
        sendToAllInLobby(lobby, new UserJoinedLobbyMessage(lobby.getLobbyId(), user));
        sendServerMessageEvent(
                lobby.getLobbyId(),
                user.getUsername() + " ist der Lobby " + lobby.getName() + " beigetreten. Willkommen!"
        );
    }

    /**
     * Handles LobbyLeaveUserRequests found on the EventBus
     * If a LobbyLeaveUserRequest is detected on the EventBus, this method is called.
     * It removes a user from a Lobby stored in the LobbyManagement and sends a
     * UserLeftLobbyMessage to every user in the lobby.
     *
     * @param request The LobbyJoinUserRequest found on the EventBus
     * @see ILobby
     * @since 2019-10-08
     */
    @Subscribe
    public void onLobbyLeaveUserRequest(LeaveLobbyRequest request) {
        LOG.debug("[LobbyId: {}] Received leave lobby request", request.getLobbyId());
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        IUserDTO user = request.getSession()
                               .orElseThrow(() -> {
                                   LOG.error(SESSION_INVALID_OR_MISSING);
                                   return new IllegalArgumentException(SESSION_INVALID_OR_MISSING);
                               })
                               .getUser();
        lobbyManagement.leaveLobby(request.getLobbyId(), UserMapper.toUser(user));
        sendToAllInLobby(lobby, new LobbyUpdatedEvent(LobbyMapper.toDTO(lobby)));
        UserLeftLobbyResponse response = new UserLeftLobbyResponse(lobby.getLobbyId());
        request.getSession()
               .ifPresent(response::setSession);
        request.getMessageContext()
               .ifPresent(response::setMessageContext);
        post(response);
        LOG.debug("[LobbyId: {}] Sent user left lobby message", request.getLobbyId());
        sendServerMessageEvent(
                lobby.getLobbyId(),
                user.getUsername() + " hat die Lobby " + lobby.getName() + " verlassen. Danke fürs mitspielen!"
        );
    }

    /**
     * Handles LobbyListRequests found on the EventBus
     * <p>
     * If a LobbyListRequest is detected on the EventBus, this method is called.
     * It retrieves the list of lobbies from the LobbyManagement and sends a
     * LobbyListResponse to the requester.
     *
     * @param request The LobbyListRequest found on the EventBus
     * @see LobbyListResponse
     * @since 2024-09-25
     */
    @Subscribe
    public void onLobbyListRequest(LobbyListRequest request) throws LobbyStoreException {
        LOG.debug("Received lobby list request");
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
        LOG.debug("Sent lobby list response");
    }

    /**
     * Handles GetLobbyRequests found on the EventBus.
     * If a GetLobbyRequest is detected on the EventBus, this method is called.
     * It retrieves the lobby information from the LobbyManagement and sends a GetLobbyResponse.
     *
     * @param request The GetLobbyRequest found on the EventBus
     * @see ILobby
     * @see GetLobbyResponse
     * @since 2024-10-02
     */
    @Subscribe
    public void onGetLobbyRequest(GetLobbyRequest request) {
        LOG.debug("[LobbyId: {}] Received get lobby request", request.getLobbyId());
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        ILobbyDTO lobbyDTO = LobbyMapper.toDTO(lobby);
        GetLobbyResponse response = new GetLobbyResponse(lobbyDTO);
        request.getMessageContext()
               .ifPresent(response::setMessageContext);
        request.getSession()
               .ifPresent(response::setSession);
        post(response);
        LOG.debug("[LobbyId: {}] Sent get lobby response", request.getLobbyId());
    }


    /**
     * Handles UpdateLobbyRequests found on the EventBus.
     * If an UpdateLobbyRequest is detected on the EventBus, this method is called.
     * It updates the lobby information in the LobbyManagement using the parameters from the request.
     *
     * @param request The UpdateLobbyRequest found on the EventBus
     * @see ILobby
     * @see ILobbyDTO
     * @since 2024-10-02
     */
    @Subscribe
    public void onUpdateLobbyRequest(UpdateLobbyRequest request) throws LobbyStoreException {
        LOG.debug(
                "[LobbyId: {}] Received update lobby request",
                request.getLobbyDTO()
                       .getLobbyId()
        );
        ILobbyDTO lobbyDTO = request.getLobbyDTO();
        ILobby updatedLobby = lobbyManagement.updateLobby(LobbyMapper.toLobby(lobbyDTO));
        sendToAllInLobby(updatedLobby, new LobbyUpdatedEvent(LobbyMapper.toDTO(updatedLobby)));
        LOG.debug("[LobbyId: {}] Sent lobby updated event", updatedLobby.getLobbyId());
    }

    /**
     * Handles RemoveUserFromLobbyRequests found on the EventBus.
     * If a RemoveUserFromLobbyRequest is detected on the EventBus, this method is called.
     * It removes a user from a Lobby stored in the LobbyManagement and sends a
     * RemovedFromLobbyEvent to the user and a LobbyUpdatedEvent to every user in the lobby.
     *
     * @param request The RemoveUserFromLobbyRequest found on the EventBus
     * @throws LobbyStoreException if there is an error accessing the lobby store
     * @see ILobby
     * @see RemovedFromLobbyEvent
     * @see LobbyUpdatedEvent
     * @since 2025-02-06
     */
    @Subscribe
    public void onRemoveUserFromLobbyRequest(RemoveUserFromLobbyRequest request) throws LobbyStoreException {
        LOG.debug("[LobbyId: {}] Received remove user from lobby request", request.getLobbyId());
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        IUser user = lobby.getUser(request.getUserToRemove());

        lobbyManagement.removeUser(request.getLobbyId(), request.getUserToRemove());

        Session session = authenticationService.getSession(user)
                                               .orElseThrow(() -> {
                                                   LOG.error(SESSION_INVALID_OR_MISSING);
                                                   return new IllegalArgumentException(SESSION_INVALID_OR_MISSING);
                                               });
        RemovedFromLobbyEvent removedFromLobbyEvent = new RemovedFromLobbyEvent(lobby.getLobbyId());
        removedFromLobbyEvent.setReceiver(List.of(session));
        post(removedFromLobbyEvent);

        sendToAllInLobby(lobby, new LobbyUpdatedEvent(LobbyMapper.toDTO(lobby)));
        LOG.debug("[LobbyId: {}] Sent lobby updated event", request.getLobbyId());
    }
}
