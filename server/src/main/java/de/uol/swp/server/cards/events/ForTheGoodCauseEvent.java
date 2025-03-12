package de.uol.swp.server.cards.events;

import de.uol.swp.common.message.AbstractMessage;
import de.uol.swp.server.message.ServerInternalMessage;
import lombok.Getter;

import java.util.Objects;

/**
 * Event to start the handling of playing the ForTheGoodCauseEventCard.
 */
@Getter
public class ForTheGoodCauseEvent  extends AbstractMessage implements ServerInternalMessage {
    private final String lobbyId;
    private final String username;

    /**
     * Constructs a new ForTheGoodCauseEvent.
     * @param lobbyId the ID of the lobby
     * @param username the username of the player
     */
    public ForTheGoodCauseEvent(String lobbyId, String username) {
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
        ForTheGoodCauseEvent event = (ForTheGoodCauseEvent) object;
        return super.equals(event) && Objects.equals(lobbyId, event.lobbyId) && Objects.equals(username,
                event.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyId, username);
    }
}
