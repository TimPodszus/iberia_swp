package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.lobby.message.AbstractLobbyMessage;
import de.uol.swp.common.user.IUserDTO;

/**
 * Message sent by the server when a user successfully joins a lobby
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.lobby.message.AbstractLobbyMessage
 * @see IUserDTO
 * @since 2019-10-08
 */
public class UserJoinedLobbyMessage extends AbstractLobbyMessage {

    /**
     * Default constructor
     *
     * @implNote this constructor is needed for serialization
     * @since 2019-10-08
     */
    public UserJoinedLobbyMessage() {
    }

    /**
     * Constructor
     *
     * @param lobbyId code of the lobby
     * @param user    user who joined the lobby
     * @since 2019-10-08
     */
    public UserJoinedLobbyMessage(String lobbyId, IUserDTO user) {
        super(lobbyId, user);
    }
}
