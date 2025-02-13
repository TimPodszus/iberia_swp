package de.uol.swp.common.lobby.message.event;

import de.uol.swp.common.message.AbstractServerMessage;
import lombok.Getter;

/**
 * Event that is triggered when a user is removed from a lobby.
 */
@Getter
public class RemovedFromLobbyEvent extends AbstractServerMessage {

    /**
     * The ID of the lobby from which the user was removed.
     */
    String lobbyId;

    /**
     * Constructs a new RemovedFromLobbyEvent.
     *
     * @param lobbyId the ID of the lobby
     */
    public RemovedFromLobbyEvent(String lobbyId) {
        this.lobbyId = lobbyId;
    }
}