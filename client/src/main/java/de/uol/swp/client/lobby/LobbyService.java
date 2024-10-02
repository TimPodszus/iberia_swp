package de.uol.swp.client.lobby;

import com.google.inject.Inject;
import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.message.request.CreateLobbyRequest;
import de.uol.swp.common.lobby.message.request.GetLobbyRequest;
import de.uol.swp.common.lobby.message.request.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.message.request.UpdateLobbyRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.greenrobot.eventbus.EventBus;

/**
 * Classes that manages lobbies
 *
 * @author Marco Grawunder
 * @since 2019-11-20
 */


public class LobbyService {

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
     * @param name Name chosen for the new lobby
     * @param user User who wants to create the new lobby
     * @see CreateLobbyRequest
     * @since 2019-11-20
     */
    public void createNewLobby(String name, UserDTO user) {
        CreateLobbyRequest createLobbyRequest = new CreateLobbyRequest(name, user);
        eventBus.post(createLobbyRequest);
    }

    /**
     * Posts a request to join a specified lobby on the EventBus
     *
     * @param name Name of the lobby the user wants to join
     * @param user User who wants to join the lobby
     * @see LobbyJoinUserRequest
     * @since 2019-11-20
     */
    public void joinLobby(String name, UserDTO user) {
        LobbyJoinUserRequest joinUserRequest = new LobbyJoinUserRequest(name, user);
        eventBus.post(joinUserRequest);
    }

    /**
     * Posts a request to update a specified lobby on the EventBus
     *
     * @param lobby The lobby to be updated
     * @param user  The user requesting the update
     * @see UpdateLobbyRequest
     * @since 2019-11-20
     */
    public void updateLobby(ILobbyDTO lobby, User user) {
        UpdateLobbyRequest updateLobbyRequest = new UpdateLobbyRequest(lobby, user);
        eventBus.post(updateLobbyRequest);
    }

    /**
     * Posts a request to get a specified lobby on the EventBus
     *
     * @param lobbyCode The code of the lobby to retrieve
     * @param user      The user requesting the lobby information
     * @see GetLobbyRequest
     * @since 2019-11-20
     */
    public void getLobby(String lobbyCode, User user) {
        GetLobbyRequest getLobbyRequest = new GetLobbyRequest(lobbyCode, user);
        eventBus.post(getLobbyRequest);
    }
}
