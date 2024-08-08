package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.UserDTO;

/**
 * Request sent to the server when a user wants to join a lobby
 *
 * @see de.uol.swp.common.lobby.message.AbstractLobbyRequest
 * @see de.uol.swp.common.user.User
 * @author Marco Grawunder
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
     * @param lobbyName name of the lobby
     * @param user user who wants to join the lobby
     * @since 2019-10-08
     */
    public LobbyJoinUserRequest(String lobbyName, UserDTO user) {
        super(lobbyName, user);
    }

}
