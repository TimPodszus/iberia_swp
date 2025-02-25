package de.uol.swp.server.cards.events;

import de.uol.swp.common.message.AbstractMessage;
import de.uol.swp.server.message.ServerInternalMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public class SecondChanceEvent extends AbstractMessage implements ServerInternalMessage {
    private final String lobbyId;
    private final String username;

    /**
     * Constructs a new SecondChanceEvent with the specified lobby ID and username.
     *
     * @param lobbyId the ID of the lobby
     * @param username the username of the player
     */
    public SecondChanceEvent(String lobbyId, String username) {
        this.lobbyId = lobbyId;
        this.username = username;
    }

    /**
     * Checks if this SecondChanceEvent is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the specified object is equal to this SecondChanceEvent, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SecondChanceEvent that = (SecondChanceEvent) o;
        return Objects.equals(lobbyId, that.lobbyId) && Objects.equals(username, that.username);
    }

    /**
     * Returns the hash code value for this SecondChanceEvent.
     *
     * @return the hash code value for this SecondChanceEvent
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyId, username);
    }
}