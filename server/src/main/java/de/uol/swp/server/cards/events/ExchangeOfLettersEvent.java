package de.uol.swp.server.cards.events;

import de.uol.swp.common.message.AbstractMessage;
import de.uol.swp.server.message.ServerInternalMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ExchangeOfLettersEvent extends AbstractMessage implements ServerInternalMessage {
    private final String lobbyId;
    private final String username;

    /**
     * Constructs a new ExchangeOfLettersEvent.
     *
     * @param lobbyId  the ID of the lobby
     * @param username the username of the player
     */
    public ExchangeOfLettersEvent(String lobbyId, String username) {
        this.lobbyId = lobbyId;
        this.username = username;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ExchangeOfLettersEvent that = (ExchangeOfLettersEvent) o;
        return Objects.equals(lobbyId, that.lobbyId) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyId, username);
    }

}
