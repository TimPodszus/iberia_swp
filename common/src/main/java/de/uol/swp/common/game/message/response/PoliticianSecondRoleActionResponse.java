package de.uol.swp.common.game.message.response;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class PoliticianSecondRoleActionResponse extends AbstractGameResponse {

    Map<String, List<ICardDTO>> cards;
    String currentPlayer;

    public PoliticianSecondRoleActionResponse(String lobbyId, Map<String, List<ICardDTO>> cards, String currentPlayer) {
        super(lobbyId, true);
        this.cards = cards;
        this.currentPlayer = currentPlayer;


    }


}
