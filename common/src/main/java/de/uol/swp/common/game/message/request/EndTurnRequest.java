package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

import java.util.Objects;

/**
 * Represents a request to end the turn in a game.
 */
public class EndTurnRequest extends AbstractGameRequest {

    /**
     * Constructs a new EndTurnRequest with the specified lobby ID.
     *
     * @param lobbyId the ID of the lobby where the turn is to be ended
     */
    public EndTurnRequest(String lobbyId) {
        super(lobbyId);
    }

    /**
     * Checks if this request is equal to another object.
     *
     * @param o the object to compare to
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
        EndTurnRequest that = (EndTurnRequest) o;
        return Objects.equals(super.getLobbyId(), that.getLobbyId());
    }

    /**
     * Returns the hash code of this request.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.getLobbyId());
    }
}