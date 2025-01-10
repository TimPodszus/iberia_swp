package de.uol.swp.common.game.message.event;

import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;

import java.util.Objects;

/**
 * Event message indicating a board update.
 * <p>
 * This class represents an event message that is sent when the game board is updated.
 * It extends the AbstractGameEvent class and includes additional information about the game.
 */
public class BoardUpdateEvent extends AbstractGameEvent {
    private final transient IGameDTO gameDTO;
    /**
     * Constructs a new BoardUpdateEvent.
     *
     * @param lobbyCode the code of the lobby associated with the event
     * @param gameDTO the game data transfer object associated with the update
     */
    public BoardUpdateEvent(String lobbyCode, IGameDTO gameDTO) {
        super(lobbyCode);
        this.gameDTO = gameDTO;
    }

    /**
     * Checks if this object is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BoardUpdateEvent that = (BoardUpdateEvent) o;
        return Objects.equals(gameDTO, that.gameDTO);
    }

    /**
     * Computes the hash code for this object.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameDTO);
    }
}