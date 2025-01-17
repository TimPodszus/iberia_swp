package de.uol.swp.common.game.message;

import de.uol.swp.common.message.request.AbstractRequestMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Abstract base class for game-related requests.
 * This class extends {@link AbstractRequestMessage} and includes a lobby ID.
 */
@Getter
@AllArgsConstructor
public abstract class AbstractGameRequest extends AbstractRequestMessage {
    /**
     * The ID of the lobby associated with this request.
     */
    private String lobbyId;

    /**
     * Checks if this request is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public abstract boolean equals(Object o);

    /**
     * Returns the hash code of this request.
     *
     * @return the hash code
     */
    @Override
    public abstract int hashCode();
}
