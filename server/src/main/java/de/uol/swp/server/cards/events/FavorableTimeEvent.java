package de.uol.swp.server.cards.events;

import de.uol.swp.common.message.AbstractMessage;
import de.uol.swp.server.message.ServerInternalMessage;
import lombok.Getter;

import java.util.Objects;

/**
 * Event representing a favorable time in the game.
 */
@Getter
public class FavorableTimeEvent extends AbstractMessage implements ServerInternalMessage {
    private final String lobbyId;
    private final String username;

    /**
     * Constructs a new FavorableTimeEvent.
     *
     * @param lobbyId  the ID of the lobby
     * @param username the username of the player
     */
    public FavorableTimeEvent(String lobbyId, String username) {
        this.lobbyId = lobbyId;
        this.username = username;
    }

    /**
     * Checks if this event is equal to another object.
     *
     * @param object the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        FavorableTimeEvent that = (FavorableTimeEvent) object;
        return lobbyId.equals(that.lobbyId) && username.equals(that.username);
    }

    /**
     * Returns the hash code of this event.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(lobbyId, username);
    }
}