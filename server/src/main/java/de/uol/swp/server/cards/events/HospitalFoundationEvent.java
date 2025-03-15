package de.uol.swp.server.cards.events;

import de.uol.swp.common.message.AbstractMessage;
import de.uol.swp.server.message.ServerInternalMessage;
import lombok.Getter;

import java.util.Objects;

/**
 * The event used when the hospital foundation event card is played.
 */
@Getter
public class HospitalFoundationEvent extends AbstractMessage implements ServerInternalMessage {
    /**
     * The ID of the lobby.
     */
    private final String lobbyId;

    /**
     * The name of the user.
     */
    private final String username;

    /**
     * Constructs a new HospitalFoundationEvent.
     *
     * @param lobbyId  the ID of the lobby
     * @param username the name of the user
     */
    public HospitalFoundationEvent(String lobbyId, String username) {
        this.lobbyId = lobbyId;
        this.username = username;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        de.uol.swp.server.cards.events.HospitalFoundationEvent that = (de.uol.swp.server.cards.events.HospitalFoundationEvent) object;
        return Objects.equals(lobbyId, that.lobbyId) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyId, username);
    }
}


