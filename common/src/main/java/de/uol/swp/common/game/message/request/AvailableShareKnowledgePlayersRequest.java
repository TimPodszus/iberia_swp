package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

public class AvailableShareKnowledgePlayersRequest extends AbstractGameRequest {

    /**
     * Constructs a new AvailableShareKnowledgePlayersRequest.
     *
     * @param lobbyId the ID of the lobby
     */
    public AvailableShareKnowledgePlayersRequest(String lobbyId) {
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
        AvailableShareKnowledgePlayersRequest that = (AvailableShareKnowledgePlayersRequest) o;
        return getLobbyId().equals(that.getLobbyId());
    }

    @Override
    public int hashCode() {
        return getLobbyId().hashCode();
    }

}
