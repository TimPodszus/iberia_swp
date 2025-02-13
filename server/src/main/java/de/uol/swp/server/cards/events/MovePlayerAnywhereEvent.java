package de.uol.swp.server.cards.events;

import de.uol.swp.common.message.AbstractMessage;
import de.uol.swp.server.message.ServerInternalMessage;
import lombok.Getter;

/**
 * Event to move a player to any location within a lobby.
 */
@Getter
public class MovePlayerAnywhereEvent extends AbstractMessage implements ServerInternalMessage {
    private final String lobbyId;
    private final String username;

    /**
     * Constructs a new MovePlayerAnywhereEvent.
     *
     * @param lobbyId  the ID of the lobby
     * @param username the username of the player
     */
    public MovePlayerAnywhereEvent(String lobbyId, String username) {
        this.lobbyId = lobbyId;
        this.username = username;
    }
}
