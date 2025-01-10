package de.uol.swp.common.game.message;

import de.uol.swp.common.message.event.AbstractEventMessage;
import lombok.Getter;

import java.util.Objects;

/**
 * Abstract base class for game event messages.
 * <p>
 * This class serves as a base for all game event messages that are sent from the server.
 * It extends the AbstractEventMessage class and includes additional information about the game and lobby.
 */
@Getter
public abstract class AbstractGameEvent extends AbstractEventMessage {
    private final String lobbyCode;

    /**
     * Constructs a new AbstractGameEvent.
     *
     * @param lobbyCode the code of the lobby associated with the event
     */
    protected AbstractGameEvent(String lobbyCode) {
        this.lobbyCode = lobbyCode;
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
        AbstractGameEvent that = (AbstractGameEvent) o;
        return Objects.equals(lobbyCode, that.lobbyCode);
    }

    /**
     * Computes the hash code for this object.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyCode);
    }
}