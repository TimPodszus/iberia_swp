package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.player.IPlayerDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class AvailableShareKnowledgePlayersResponse extends AbstractGameResponse {

    private final List<IPlayerDTO> players;


    public AvailableShareKnowledgePlayersResponse(String lobbyId, List<IPlayerDTO> players) {
        super(lobbyId, true);
        this.players = players;
    }


}
