package de.uol.swp.common.game.message.event;

import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;
import lombok.Getter;

import java.util.Objects;

/**
 * Event message indicating the start of a game.
 * <p>
 * This class represents an event message that is sent when a game starts.
 * It extends the AbstractGameEvent class and includes additional information about the game.
 */
@Getter
public class StartGameEvent extends AbstractGameEvent {
    private final IGameDTO gameDTO;

    /**
     * Constructs a new StartGameEvent.
     *
     * @param lobbyCode the code of the lobby associated with the event
     * @param game the game data transfer object associated with the started game
     */
    public StartGameEvent(String lobbyCode, IGameDTO game) {
        super(lobbyCode);
        this.gameDTO = game;
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
        StartGameEvent that = (StartGameEvent) o;
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