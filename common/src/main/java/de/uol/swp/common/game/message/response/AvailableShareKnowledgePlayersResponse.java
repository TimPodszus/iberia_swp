package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.player.IPlayerDTO;
import lombok.Getter;

import java.util.List;

/**
 * Response message containing the list of players available for sharing knowledge.
 */
@Getter
public class AvailableShareKnowledgePlayersResponse extends AbstractGameResponse {

    private final List<IPlayerDTO> players;

    /**
     * Constructs a new AvailableShareKnowledgePlayersResponse.
     *
     * @param lobbyId the ID of the lobby
     * @param players the list of players available for sharing knowledge
     */
    public AvailableShareKnowledgePlayersResponse(String lobbyId, List<IPlayerDTO> players) {
        super(lobbyId, true);
        this.players = players;
    }

}