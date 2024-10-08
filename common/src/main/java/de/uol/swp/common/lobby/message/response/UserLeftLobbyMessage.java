package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.lobby.message.AbstractLobbyMessage;
import de.uol.swp.common.user.User;

/**
 * Message sent by the server when a user successfully leaves a lobby
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.lobby.message.AbstractLobbyMessage
 * @see de.uol.swp.common.user.User
 * @since 2019-10-08
 */
public class UserLeftLobbyMessage extends AbstractLobbyMessage {

    /**
     * Default constructor
     *
     * @implNote this constructor is needed for serialization
     * @since 2019-10-08
     */
    public UserLeftLobbyMessage() {
    }

    /**
     * Constructor
     *
     * @param lobbyCode code of the lobby
     * @param user      user who left the lobby
     * @since 2019-10-08
     */
    public UserLeftLobbyMessage(String lobbyCode, User user) {
        super(lobbyCode, user);
    }
}
