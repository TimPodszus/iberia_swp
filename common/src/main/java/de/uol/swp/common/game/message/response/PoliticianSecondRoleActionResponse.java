package de.uol.swp.common.game.message.response;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * Response message for the second role action of a politician in the game.
 */
@Getter
public class PoliticianSecondRoleActionResponse extends AbstractGameResponse {

    /**
     * A map containing the cards associated with each player.
     */
    Map<String, List<ICardDTO>> cards;

    /**
     * The current player performing the action.
     */
    String currentPlayer;

    /**
     * Constructs a new PoliticianSecondRoleActionResponse.
     *
     * @param lobbyId       the ID of the lobby
     * @param cards         the map of cards associated with each player
     * @param currentPlayer the current player performing the action
     */
    public PoliticianSecondRoleActionResponse(String lobbyId, Map<String, List<ICardDTO>> cards, String currentPlayer) {
        super(lobbyId, true);
        this.cards = cards;
        this.currentPlayer = currentPlayer;
    }
}