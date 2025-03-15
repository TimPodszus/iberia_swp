package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

import java.util.Objects;

/**
 * Represents a request for a user to leave a game.
 * <p>
 * This class extends the AbstractGameRequest and includes the lobby ID of the game the user is leaving.
 */
public class UserLeavedGameRequest extends AbstractGameRequest {

    /**
     * Constructs a new UserLeavedGameRequest with the specified lobby ID.
     *
     * @param lobbyId the ID of the lobby the user is leaving
     */
    public UserLeavedGameRequest(String lobbyId) {
        super(lobbyId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserLeavedGameRequest that = (UserLeavedGameRequest) o;
        return Objects.equals(getLobbyId(), that.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId());
    }
}