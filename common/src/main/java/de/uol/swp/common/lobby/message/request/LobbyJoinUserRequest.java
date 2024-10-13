package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.message.AbstractLobbyRequest;
import de.uol.swp.common.user.UserDTO;

/**
 * Request sent to the server when a user wants to join a lobby
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.lobby.message.AbstractLobbyRequest
 * @see de.uol.swp.common.user.User
 * @since 2019-10-08
 */
public class LobbyJoinUserRequest extends AbstractLobbyRequest {

    /**
     * Default constructor
     *
     * @implNote this constructor is needed for serialization
     * @since 2019-10-08
     */
    public LobbyJoinUserRequest() {
    }

    /**
     * Constructor
     *
     * @param lobbyCode name of the lobby
     * @param user user who wants to join the lobby
     * @since 2019-10-08
     */
    public LobbyJoinUserRequest(String lobbyCode, UserDTO user) {
        super(lobbyCode, user);
    }

}
