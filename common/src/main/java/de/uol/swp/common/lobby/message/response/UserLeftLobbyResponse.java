package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.user.IUserDTO;
import lombok.Getter;

/**
 * Message sent by the server when a user successfully leaves a lobby
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.lobby.message.AbstractLobbyMessage
 * @see IUserDTO
 * @since 2019-10-08
 */
@Getter
public class UserLeftLobbyResponse extends AbstractResponseMessage {
    String lobbyId;

    /**
     * Default constructor
     *
     * @implNote this constructor is needed for serialization
     * @since 2019-10-08
     */
    public UserLeftLobbyResponse() {
    }

    /**
     * Constructor
     *
     * @param lobbyId code of the lobby
     * @since 2019-10-08
     */
    public UserLeftLobbyResponse(String lobbyId) {
        this.lobbyId = lobbyId;
    }
}
