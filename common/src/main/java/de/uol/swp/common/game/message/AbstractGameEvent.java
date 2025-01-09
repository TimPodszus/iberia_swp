package de.uol.swp.common.game.message;

import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.message.event.AbstractEventMessage;
import lombok.Getter;

/**
 * Abstract base class for game event messages.
 * <p>
 * This class serves as a base for all game event messages that are sent from the server.
 * It extends the AbstractEventMessage class and includes additional information about the game and lobby.
 */
@Getter
public abstract class AbstractGameEvent extends AbstractEventMessage {
    private final String lobbyCode;
    private final IGameDTO game;

    /**
     * Constructs a new AbstractGameEvent.
     *
     * @param lobbyCode the code of the lobby associated with the event
     * @param game      the game data transfer object associated with the event
     */
    protected AbstractGameEvent(String lobbyCode, IGameDTO game) {
        this.lobbyCode = lobbyCode;
        this.game = game;
    }
}