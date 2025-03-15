package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.message.AbstractLobbyRequest;

/**
 * A request to get the details of a specific lobby.
 */
public class GetLobbyRequest extends AbstractLobbyRequest {

    /**
     * Constructs a new GetLobbyRequest with the specified lobby ID.
     *
     * @param lobbyId the ID of the lobby to retrieve
     */
    public GetLobbyRequest(String lobbyId) {
        super(lobbyId);
    }
}