package de.uol.swp.common.plague.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

import java.util.Objects;

/**
 * Represents a request to research a plague in the game.
 */
public class ResearchPlagueRequest extends AbstractGameRequest {

    public ResearchPlagueRequest(String lobbyId) {
        super(lobbyId);
    }

    /**
     * Checks if this request is equal to another object.
     * Two ResearchPlagueRequest objects are considered equal if they have the same lobbyId.
     *
     * @param o the object to compare with
     * @return {@code true} if the objects are equal, otherwise {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResearchPlagueRequest that = (ResearchPlagueRequest) o;
        return Objects.equals(getLobbyId(), that.getLobbyId());
    }

    /**
     * Returns the hash code for this request.
     * The hash code is based on the lobbyId.
     *
     * @return the computed hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId());
    }
}
