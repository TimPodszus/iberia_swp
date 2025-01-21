package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.EpidemicCard;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.management.CityManagement;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;
import java.util.Objects;

public class PlayerManagement implements IPlayerManagement {
    public ICardDTO drawPlayerCard(IGame game, IUser user) throws PlayerManagementException {
        Player player = game.getPlayers()
                            .stream()
                            .filter(p -> Objects.equals(
                                    p.getUser()
                                     .getUsername(), user.getUsername()
                            ))
                            .findFirst()
                            .orElseThrow(() -> new PlayerManagementException("Player not found for the given user"));
        return drawPlayerCard(game, player);
    }

    public ICardDTO drawPlayerCard(IGame game, Player player) throws PlayerManagementException {
        Card card = getCard(game, player);

        if (card instanceof EpidemicCard) {
            game.setInfectionCounter(game.getInfectionCounter() + 1);

            IGameManagement gameManagement = new GameManagement();
            InfectionCard epidemicCard = gameManagement.drawInfectionCard(game);

            CityManagement cityManagement = new CityManagement();
            cityManagement.infectCityWithOwnPlague(game, epidemicCard, 3);

            game.getPlayerCardDiscardPile()
                .add(card);
        } else {
            game.getPlayers()
                .get(game.getCurrentPlayerIndex())
                .addCard(card);
        }

        return CardMapper.toDTO(card);
    }

    private Card getCard(IGame game, Player player) throws PlayerManagementException {
        if (!player.equals(game.getPlayers()
                               .get(game.getCurrentPlayerIndex())) || !(game.getState() instanceof DrawCardState)) {
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