package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.message.AbstractLobbyRequest;
import de.uol.swp.common.user.IUserDTO;


/**
 * Request sent to the server when a user wants to leave a lobby
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.lobby.message.AbstractLobbyRequest
 * @see IUserDTO
 * @since 2019-10-08
 */
public class LobbyLeaveUserRequest extends AbstractLobbyRequest {

    /**
     * Default constructor
     *
     * @implNote this constructor is needed for serialization
     * @since 2019-10-08
     */
    public LobbyLeaveUserRequest() {
    }

    /**
     * Constructor
     *
     * @param lobbyName name of the lobby
     * @param user      user who wants to leave the lobby
     * @since 2019-10-08
     */
    public LobbyLeaveUserRequest(String lobbyName, IUserDTO user) {
        super(lobbyName, user);
    }

}
