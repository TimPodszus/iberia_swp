package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.player.request.DrawPlayerCardRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.EpidemicCard;
import de.uol.swp.server.game.data.IGame;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PlayerManagement implements IPlayerManagement {
    private IGame game;

    public ICardDTO drawPlayerCard(IGame game, DrawPlayerCardRequest request) throws PlayerManagementException {
        this.game = game;

        Card card = getCard(request);

        if (card instanceof EpidemicCard) {
            //TODO: implement city infection
        } else {
            game.getPlayers()
                .get(game.getCurrentPlayerIndex())
                .addCard(card);
        }

        return CardMapper.toDTO(card);
    }

    private Card getCard(DrawPlayerCardRequest request) throws PlayerManagementException {
        User user = request.getSession()
                           .orElseThrow(() -> new IllegalStateException("Session not present"))
                           .getUser();

        if (user != game.getPlayers()
                        .get(game.getCurrentPlayerIndex())
                        .getUser()) {
            throw new PlayerManagementException();
        }

        List<Card> playerCardDrawPile = game.getPlayerCardDrawPile();

        if (playerCardDrawPile.isEmpty()) {
            throw new PlayerManagementException();
        }

        // if 0 is the top card of the draw pile
        return playerCardDrawPile.remove(0);
    }
}