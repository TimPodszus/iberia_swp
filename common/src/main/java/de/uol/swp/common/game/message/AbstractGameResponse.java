package de.uol.swp.common.game.message;

import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * Abstract base class for game response messages.
 * Provides common properties and methods for game responses.
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class AbstractGameResponse extends AbstractResponseMessage {
    /**
     * The ID of the lobby associated with the response.
     */
    private final String lobbyId;

    /**
     * Indicates whether the response was successful.
     */
    private final boolean success;

    /**
     * Optional description of the response.
     */
    private String description;

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
        AbstractGameResponse that = (AbstractGameResponse) o;
        return Objects.equals(this.lobbyId, that.getLobbyId()) && Objects.equals(this.success,
                that.isSuccess()
        ) && Objects.equals(this.description, that.getDescription());
    }

    /**
     * Computes the hash code for this object.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyId, success, description);
    }
}