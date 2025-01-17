package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

import java.util.Objects;

/**
 * A request to get the available actions in a game lobby.
 */
public class AvailableActionsRequest extends AbstractGameRequest {

    /**
     * Constructs a new AvailableActionsRequest.
     *
     * @param lobbyId the ID of the lobby
     */
    public AvailableActionsRequest(String lobbyId) {
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
        AvailableActionsRequest that = (AvailableActionsRequest) o;
        return Objects.equals(super.getLobbyCode(), that.getLobbyCode());
    }

    /**
     * Returns the hash code of this request.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.getLobbyCode());
    }
}
