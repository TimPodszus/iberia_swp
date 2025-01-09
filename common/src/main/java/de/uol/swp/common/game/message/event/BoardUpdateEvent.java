package de.uol.swp.common.game.message.event;

import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;

/**
 * Event message indicating a board update.
 * <p>
 * This class represents an event message that is sent when the game board is updated.
 * It extends the AbstractGameEvent class and includes additional information about the game.
 */
public class BoardUpdateEvent extends AbstractGameEvent {
    /**
     * Constructs a new BoardUpdateEvent.
     *
     * @param lobbyCode the code of the lobby associated with the event
     * @param gameDTO the game data transfer object associated with the update
     */
    public BoardUpdateEvent(String lobbyCode, IGameDTO gameDTO) {
        super(lobbyCode, gameDTO);
    }
}