package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.message.AbstractLobbyRequest;

/**
 * Request sent to the server when a user wants to leave a lobby
 *
 * @author Marco Grawunder
 * @see de.uol.swp.common.lobby.message.AbstractLobbyRequest
 * @since 2019-10-08
 */
public class LeaveLobbyRequest extends AbstractLobbyRequest {

    /**
     * Default constructor
     *
     * @implNote this constructor is needed for serialization
     * @since 2019-10-08
     */
    public LeaveLobbyRequest() {
    }

    /**
     * Constructor
     *
     * @param lobbyId id of the lobby
     * @since 2019-10-08
     */
    public LeaveLobbyRequest(String lobbyId) {
        super(lobbyId);
    }

}
