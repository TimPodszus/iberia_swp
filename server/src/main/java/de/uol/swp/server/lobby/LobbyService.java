package de.uol.swp.server.lobby;

import com.google.inject.Inject;
import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.event.RemovedFromLobbyEvent;
import de.uol.swp.common.lobby.message.request.*;
import de.uol.swp.common.lobby.message.response.*;
import de.uol.swp.common.message.response.ExceptionMessage;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.exceptions.LobbyIsFullException;
import de.uol.swp.server.lobby.exceptions.LobbyNotFoundException;
import de.uol.swp.server.lobby.exceptions.UserAlreadyInLobbyException;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
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
    public void onCreateLobbyRequest(CreateLobbyRequest createLobbyRequest) {
        LOG.debug("Received create lobby request");
        Session session = createLobbyRequest.getSession()
                                            .orElseThrow(() -> {
                                                LOG.error("Session not found in CreateLobbyRequest");
                                                return new SessionNotFoundException();
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
                "Die Lobby wurde erstellt."
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
    public void onLobbyJoinUserRequest(JoinLobbyRequest request) {
        LOG.debug("[LobbyId: {}] Received join lobby request", request.getLobbyId());
        Session session = request.getSession()
                                 .orElseThrow(() -> {
                                     LOG.error("[LobbyId: {}] Session is missing or invalid", request.getLobbyId());
                                     return new SessionNotFoundException();
                                 });
        IUserDTO user = session.getUser();

        try {
            lobbyManagement.joinLobby(request.getLobbyId(), UserMapper.toUser(user));
        } catch (LobbyNotFoundException e) {
            LOG.error("[LobbyId: {}] Lobby to join not found", request.getLobbyId());
            ExceptionMessage exceptionMessage = new ExceptionMessage("Der Lobby konnte nicht beigetreten werden. Keine Lobby mit der ID " + request.getLobbyId() + " gefunden.");
            exceptionMessage.setSession(session);
            request.getMessageContext()
                   .ifPresent(exceptionMessage::setMessageContext);
            post(exceptionMessage);

            return;
        } catch (LobbyIsFullException e) {
            LOG.error("[LobbyId: {}] Lobby to join is full", request.getLobbyId());
            ExceptionMessage exceptionMessage = new ExceptionMessage("Der Lobby konnte nicht beigetreten werden. Die Lobby ist bereits voll.");
            exceptionMessage.setSession(session);
            request.getMessageContext()
                   .ifPresent(exceptionMessage::setMessageContext);
            post(exceptionMessage);

            return;
        } catch (UserAlreadyInLobbyException e) {
            LOG.error("[LobbyId: {}] Lobby join failed. User is already in Lobby", request.getLobbyId());
            ExceptionMessage exceptionMessage = new ExceptionMessage("Der Lobby konnte nicht beigetreten werden. Du bist bereits in der Lobby.");
            exceptionMessage.setSession(session);
            request.getMessageContext()
                   .ifPresent(exceptionMessage::setMessageContext);
            post(exceptionMessage);

            return;
        }

        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        LOG.info("[LobbyId: {}] User joined lobby", request.getLobbyId());
        sendToAllInLobby(lobby, new UserJoinedLobbyMessage(lobby.getLobbyId(), user));
        sendServerMessageEvent(
                lobby.getLobbyId(),
                user.getUsername() + " ist der Lobby beigetreten. Willkommen!"
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
        Session session = request.getSession()
                                 .orElseThrow(() -> {
                                     LOG.error("[LobbyId: {}] Session is missing or invalid", request.getLobbyId());
                                     return new SessionNotFoundException();
                                 });
        IUserDTO user = session.getUser();

        lobbyManagement.leaveLobby(request.getLobbyId(), UserMapper.toUser(user));

        sendToAllInLobby(lobby, new LobbyUpdatedEvent(LobbyMapper.toDTO(lobby)));
        UserLeftLobbyResponse response = new UserLeftLobbyResponse(lobby.getLobbyId());
        response.setSession(session);
        request.getMessageContext()
               .ifPresent(response::setMessageContext);
        post(response);
        LOG.info("[LobbyId: {}] Sent user left lobby message", request.getLobbyId());
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
    public void onLobbyListRequest(LobbyListRequest request) {
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
    public void onUpdateLobbyRequest(UpdateLobbyRequest request) {
        LOG.debug(
                "[LobbyId: {}] Received update lobby request",
                request.getLobbyDTO()
                       .getLobbyId()
        );
        ILobbyDTO lobbyDTO = request.getLobbyDTO();
        ILobby updatedLobby;

        try {
            updatedLobby = lobbyManagement.updateLobby(LobbyMapper.toLobby(lobbyDTO));
        } catch (LobbyNotFoundException e) {
            LOG.error("[LobbyId: {}] Lobby not found", request.getLobbyId());
            ExceptionMessage exceptionMessage = new ExceptionMessage(
                    "Lobby konnte nicht aktualisiert werden. Keine Lobby mit der ID " + request.getLobbyId() + " gefunden.");
            request.getSession()
                   .ifPresent(exceptionMessage::setSession);
            request.getMessageContext()
                   .ifPresent(exceptionMessage::setMessageContext);
            post(exceptionMessage);

            return;
        }
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
     * @see ILobby
     * @see RemovedFromLobbyEvent
     * @see LobbyUpdatedEvent
     * @since 2025-02-06
     */
    @Subscribe
    public void onRemoveUserFromLobbyRequest(RemoveUserFromLobbyRequest request) {
        LOG.debug("[LobbyId: {}] Received remove user from lobby request", request.getLobbyId());
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        IUser user = lobby.getUser(request.getUserToRemove());

        try {
            lobbyManagement.removeUser(request.getLobbyId(), request.getUserToRemove());
        } catch (LobbyNotFoundException e) {
            LOG.error("[LobbyId: {}] Lobby not found", request.getLobbyId());
            ExceptionMessage exceptionMessage = new ExceptionMessage(
                    "User konnte nicht rausgeschmissen werden. Keine Lobby mit der ID " + request.getLobbyId() + " gefunden.");
            request.getSession()
                   .ifPresent(exceptionMessage::setSession);
            request.getMessageContext()
                   .ifPresent(exceptionMessage::setMessageContext);
            post(exceptionMessage);

            return;
        }

        Session session = authenticationService.getSession(user)
                                               .orElseThrow(() -> {
                                                   LOG.error(
                                                           "[LobbyId: {}] Session not found for user",
                                                           request.getLobbyId()
                                                   );
                                                   return new SessionNotFoundException();
                                               });
        RemovedFromLobbyEvent removedFromLobbyEvent = new RemovedFromLobbyEvent(lobby.getLobbyId());
        removedFromLobbyEvent.setReceiver(List.of(session));
        post(removedFromLobbyEvent);

        sendToAllInLobby(lobby, new LobbyUpdatedEvent(LobbyMapper.toDTO(lobby)));
        LOG.debug("[LobbyId: {}] Sent lobby updated event", request.getLobbyId());
    }
}
