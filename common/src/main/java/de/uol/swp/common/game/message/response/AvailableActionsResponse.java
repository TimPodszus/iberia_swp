package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * Response message containing the available actions in the game.
 */
@Getter
public class AvailableActionsResponse extends AbstractGameResponse {
    List<GameActions> availableActions;

    /**
     * Constructs a new AvailableActionsResponse.
     *
     * @param lobbyId          the ID of the lobby
     * @param success          whether the response indicates success
     * @param availableActions the list of available game actions
     */
    public AvailableActionsResponse(
            String lobbyId, boolean success, String description, List<GameActions> availableActions
    ) {
        super(lobbyId, success, description);
        this.availableActions = availableActions;
    }

    /**
     * Compares this AvailableActionsResponse to the specified object.
     *
     * @param object the object to compare this AvailableActionsResponse against
     * @return true if the given object represents an AvailableActionsResponse equivalent to this response, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AvailableActionsResponse that = (AvailableActionsResponse) object;
        return Objects.equals(availableActions, that.availableActions);
    }

    /**
     * Returns a hash code value for this AvailableActionsResponse.
     *
     * @return a hash code value for this AvailableActionsResponse
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), availableActions);
    }
}
