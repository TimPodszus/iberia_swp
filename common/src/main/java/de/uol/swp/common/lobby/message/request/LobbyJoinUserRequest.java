package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.message.AbstractLobbyRequest;
import lombok.NoArgsConstructor;


/**
 * Request sent to the server when a user wants to join a lobby
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.lobby.message.AbstractLobbyRequest
 * @since 2019-10-08
 */
@NoArgsConstructor
public class LobbyJoinUserRequest extends AbstractLobbyRequest {

    /**
     * Constructor
     *
     * @param lobbyId name of the lobby
     * @since 2019-10-08
     */
    public LobbyJoinUserRequest(String lobbyId) {
        super(lobbyId);
    }

}
