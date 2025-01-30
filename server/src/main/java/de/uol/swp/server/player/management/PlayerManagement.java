package de.uol.swp.server.player.management;

import com.google.inject.Inject;
import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.cards.*;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.CityManagement;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.StartState;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;
import java.util.Objects;


public class PlayerManagement implements IPlayerManagement {
    @Inject
    private final IGameManagement gameManagement = new GameManagement();

    @Inject
    private final ICityManagement cityManagement = new CityManagement();

    public ICardDTO drawPlayerCard(String lobbyCode, IUser user) throws PlayerManagementException {
        IPlayer player = GameStore.getInstance().getGame(lobbyCode).getPlayers()
                             .stream()
                             .filter(p -> Objects.equals(p.getUser()
                                                          .getUsername(), user.getUsername()))
                             .findFirst()
                             .orElseThrow(() -> new PlayerManagementException("Player not found for the given user"));
        return drawPlayerCard(lobbyCode, player);
    }

    public ICardDTO drawPlayerCard(
            String lobbyCode, IPlayer player
    ) throws PlayerManagementException {
        IGame game = GameStore.getInstance().getGame(lobbyCode);

        ICard card = getCard(game, player);

        if (card instanceof EpidemicCard) {
            game.setInfectionCounter(game.getInfectionCounter() + 1);

            InfectionCard epidemicCard = gameManagement.drawInfectionCard(game);
            cityManagement.infectCityWithOwnPlague(game, epidemicCard, 3);

            game.getPlayerCardDiscardPile()
                .add(card);
        } else {
            addCard(player, card);
        }
        if (game.getState() instanceof DrawCardState drawCardState) {
            drawCardState.increaseCardsDrawn(game);
        }
        return CardMapper.toDTO(card);
    }

    private ICard getCard(IGame game, IPlayer player) throws PlayerManagementException {
        if (!player.equals(game.getPlayers()
                               .get(game.getCurrentPlayerIndex())) || !(game.getState() instanceof DrawCardState) && !(game.getState() instanceof StartState)) {
            throw new PlayerManagementException("It is not the player's turn to draw a card");
        }

        List<ICard> playerCardDrawPile = game.getPlayerCardDrawPile();

        if (playerCardDrawPile.isEmpty()) {
            throw new PlayerManagementException("Player card draw pile is empty");
        }

        // if 0 is the top card of the draw pile
        return playerCardDrawPile.remove(0);
    }

    public void setStartingPosition(String lobbyCode, CityName cityName, IPlayer player) throws PlayerManagementException {
        IGame game = GameStore.getInstance().getGame(lobbyCode);

        boolean validRequest = false;
        int cityCardCount = 0;
        for (ICard card : player.getCards()) {
            if (card instanceof CityCard cityCard) {
                cityCardCount++;
                if (cityCard.getCity()
                            .getName()
                            .equals(cityName)) {
                    validRequest = true;
                }
            }
        }
        if (validRequest || cityCardCount == 0) {
            ICity city = game.getCityRepository().getCitiesByNames(cityName)
                                       .get(0);
            player.setCurrentPosition(city);
        } else {
            throw new PlayerManagementException(
                    "Keine valide Stadt ausgewählt! Du musst eine Stadt die du auf der Hand hast auswählen!");
        }
    }

    public void addCard(IPlayer player, ICard card) {
        player.getCards().add(card);
    }

    public void discardCard(String lobbyCode, IPlayer player, ICard card) {
        discardCards(lobbyCode, player, List.of(card));
    }

    public void discardCards(String lobbyCode, IPlayer player, List<? extends ICard> cards) {
        IGame game = GameStore.getInstance().getGame(lobbyCode);

        for (ICard card : cards) {
            player.getCards().remove(card);
        }
        game.getPlayerCardDiscardPile().addAll(cards);
    }

    public Card getCard(String lobbyId, String playerName, int cardId) throws PlayerManagementException {
        IGame game = GameStore.getInstance()
                              .getGame(lobbyId);
        IPlayer player = getPlayer(game, playerName);
        return player.getCards()
                     .stream()
                     .filter(c -> Objects.equals(c.getId(), cardId))
                     .findFirst()
                     .orElse(null);
    }

    private IPlayer getPlayer(IGame game, String playerName) throws PlayerManagementException {
        return game.getPlayers()
                   .stream()
                   .filter(p -> p.getUser()
                                 .getUsername()
                                 .equals(playerName))
                   .findFirst()
                   .orElseThrow(() -> new PlayerManagementException("Player not found"));
    }
}