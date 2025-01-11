package de.uol.swp.server.lobby;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.request.LobbyListRequest;
import de.uol.swp.common.lobby.message.response.*;
import de.uol.swp.common.lobby.message.request.*;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import de.uol.swp.server.usermanagement.UserMapper;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.sql.SQLException;
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
    /**
     * Constructs a new LobbyService.
     * <p>
     * This constructor is used to create an instance of LobbyService and
     * initializes it with the provided EventBus.
     *
     * @param eventBus the EventBus to be used by this service
     */
    @Inject
    public LobbyService(EventBus eventBus) {
        super(eventBus);
    }

    /**
     * Handles CreateLobbyRequests found on the EventBus
     * If a CreateLobbyRequest is detected on the EventBus, this method is called.
     * It creates a new Lobby via the LobbyManagement using the parameters from the
     * request and sends a LobbyCreatedMessage to every connected user
     *
     * @param createLobbyRequest The CreateLobbyRequest found on the EventBus
     * @see LobbyCreatedMessage
     * @since 2019-10-08
     */
    @Subscribe
    public void onCreateLobbyRequest(CreateLobbyRequest createLobbyRequest) throws LobbyManagementException {
        ILobby createdLobby = lobbyManagement.createLobby(createLobbyRequest.getLobbyCode(),
                UserMapper.toUser(createLobbyRequest.getOwner())
        );
        sendToAll(new LobbyCreatedMessage(createdLobby.getName(), createLobbyRequest.getOwner()));
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
    public void onLobbyJoinUserRequest(LobbyJoinUserRequest lobbyJoinUserRequest) throws LobbyManagementException, SQLException {
        Optional<ILobby> optionalLobby = lobbyManagement.getLobby(lobbyJoinUserRequest.getLobbyCode());
        if (optionalLobby.isPresent()) {
            ILobby lobby = optionalLobby.get();
            lobbyManagement.joinLobby(lobby, UserMapper.toUser(lobbyJoinUserRequest.getUser()));
            sendToAllInLobby(
                    lobbyJoinUserRequest.getLobbyCode(),
                    new UserJoinedLobbyMessage(lobbyJoinUserRequest.getLobbyCode(), lobbyJoinUserRequest.getUser())
            );
        } else {
            throw new LobbyManagementException("Lobby not found");
        }
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
                 .leaveUser(UserMapper.toUser(lobbyLeaveUserRequest.getUser()));
            sendToAllInLobby(lobbyLeaveUserRequest.getLobbyCode(),
                    new UserLeftLobbyMessage(lobbyLeaveUserRequest.getLobbyCode(), lobbyLeaveUserRequest.getUser())
            );
        } else {
            throw new LobbyManagementException("Lobby not found");
        }
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
        ILobby updatedLobby = lobbyManagement.updateLobby(LobbyMapper.toLobby(lobbyDTO));
        sendToAllInLobby(updatedLobby.getLobbyCode(), new LobbyUpdatedEvent(LobbyMapper.toDTO(updatedLobby)));
    }
}
