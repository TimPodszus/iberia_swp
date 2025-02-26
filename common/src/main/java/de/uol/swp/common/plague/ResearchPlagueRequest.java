package de.uol.swp.common.plague;

import lombok.Getter;

import java.util.Objects;

/**
 * A request message sent to research a specific plague.
 * This class represents a message that is sent when a player
 * requests to research a particular plague. It contains information about
 * the plague being researched, such as its name.
 */
public class ResearchPlagueRequest extends AbstractPlagueMessage {

    @Getter
    private final String lobbyId;

    public ResearchPlagueRequest(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPlagueRequest request)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getLobbyId(), request.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLobbyId());
    }
}
