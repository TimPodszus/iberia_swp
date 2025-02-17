package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.message.AbstractLobbyRequest;
import lombok.Getter;

import java.io.Serializable;

/**
 * Request to remove a user from a lobby.
 */
@Getter
public class RemoveUserFromLobbyRequest extends AbstractLobbyRequest implements Serializable {

    /**
     * The username of the user to be removed from the lobby.
     */
    private final String userToRemove;

    /**
     * Constructs a new RemoveUserFromLobbyRequest.
     *
     * @param lobbyId      the ID of the lobby
     * @param userToRemove the username of the user to be removed
     */
    public RemoveUserFromLobbyRequest(String lobbyId, String userToRemove) {
        super(lobbyId);
        this.userToRemove = userToRemove;
    }

}
