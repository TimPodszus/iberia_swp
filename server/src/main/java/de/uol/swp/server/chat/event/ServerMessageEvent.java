package de.uol.swp.server.chat.event;

import de.uol.swp.common.message.AbstractMessage;
import de.uol.swp.server.message.ServerInternalMessage;
import lombok.Getter;

/**
 * Event representing a server message.
 */
@Getter
public class ServerMessageEvent extends AbstractMessage implements ServerInternalMessage {
    private final String lobbyId;
    private final String message;

    /**
     * Constructs a new ServerMessageEvent with the specified message.
     *
     * @param lobbyId the ID of the lobby
     * @param message the message to be sent with the event
     */
    public ServerMessageEvent(String lobbyId, String message) {
        this.lobbyId = lobbyId;
        this.message = message;
    }
}