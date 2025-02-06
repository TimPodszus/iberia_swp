package de.uol.swp.client.lobby;

import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.lobby.message.request.*;
import de.uol.swp.common.user.IUserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.lobby.dto.ILobbyDTO;

import java.util.List;


/**
 * Classes that manages lobbies
 *
 * @author Marco Grawunder
 * @since 2019-11-20
 */


public class LobbyService {
    private static final Logger LOG = LogManager.getLogger(LobbyService.class);

    private final EventBus eventBus;

    /**
     * Constructor
     *
     * @param eventBus The EventBus set in ClientModule
     * @see de.uol.swp.client.di.ClientModule
     * @since 2019-11-20
     */
    @Inject
    public LobbyService(EventBus eventBus) {
        this.eventBus = eventBus;

    }

    /**
     * Posts a request to create a lobby on the EventBus
     *
     * @see CreateLobbyRequest
     * @since 2019-11-20
     */
    public void createNewLobby() {
        CreateLobbyRequest createLobbyRequest = new CreateLobbyRequest();
        eventBus.post(createLobbyRequest);
        LOG.info("Send CreateLobbyRequest");
    }

    public void requestLobbyList() {
        eventBus.post(new LobbyListRequest());
        LOG.info("Send LobbyListRequest");
    }


    /**
     * Posts a request to update a specified lobby on the EventBus
     *
     * @param lobby The lobby to be updated
     * @see UpdateLobbyRequest
     * @since 2024-10-08
     */
    public void updateLobby(ILobbyDTO lobby) {
        UpdateLobbyRequest updateLobbyRequest = new UpdateLobbyRequest(lobby);
        eventBus.post(updateLobbyRequest);
        LOG.info("Send UpdateLobbyRequest");
    }

    /**
     * Posts a request to get a specified lobby on the EventBus
     *
     * @param lobbyId The id of the lobby to retrieve
     * @see GetLobbyRequest
     * @since 2024-10-08
     */
    public void getLobby(String lobbyId) {
        GetLobbyRequest getLobbyRequest = new GetLobbyRequest(lobbyId);
        eventBus.post(getLobbyRequest);
        LOG.info("Send GetLobbyRequest");
    }

    /**
     * Posts a request to join a specified lobby on the EventBus
     *
     * @param lobbyId The id of the lobby to join
     * @see LobbyJoinUserRequest
     * @since 2024-10-08
     */
    public void joinLobby(String lobbyId) {
        LobbyJoinUserRequest lobbyJoinUserRequest = new LobbyJoinUserRequest(lobbyId);
        eventBus.post(lobbyJoinUserRequest);
        LOG.info("Send LobbyJoinUserRequest");
    }

    /**
     * Posts a request to remove a user from a specified lobby on the EventBus
     *
     * @param lobbyId  The id of the lobby from which the user will be removed
     * @param username The username of the user to be removed
     * @see RemoveUserFromLobbyRequest
     * @since 2024-10-08
     */
    public void removeUser(String lobbyId, String username) {
        RemoveUserFromLobbyRequest removeUserFromLobbyRequest = new RemoveUserFromLobbyRequest(lobbyId, username);
        eventBus.post(removeUserFromLobbyRequest);
        LOG.info("Send RemoveUserFromLobbyRequest");
    }

    /**
     * Posts a request to leave a specified lobby on the EventBus
     *
     * @param lobbyId The id of the lobby to leave
     * @see LobbyLeaveUserRequest
     * @since 2024-10-08
     */
    public void leaveLobby(String lobbyId) {
        LobbyLeaveUserRequest lobbyLeaveUserRequest = new LobbyLeaveUserRequest(lobbyId);
        eventBus.post(lobbyLeaveUserRequest);
        LOG.info("Send LobbyLeaveUserRequest");
    }

    /**
     * Posts a request to start a game in a specified lobby on the EventBus
     *
     * @param lobbyId    The id of the lobby where the game will start
     * @param difficulty The difficulty level of the game
     * @param users      The list of users participating in the game
     * @see CreateGameRequest
     * @since 2024-10-08
     */
    public void startGame(String lobbyId, int difficulty, List<IUserDTO> users) {
        CreateGameRequest createGameRequest = new CreateGameRequest(lobbyId, difficulty, users);
        eventBus.post(createGameRequest);
        LOG.info("Send CreateGameRequest");
    }
}
