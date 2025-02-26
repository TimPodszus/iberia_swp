package de.uol.swp.server.player.management;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.cards.*;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.EpidemicCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.game.states.StartState;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.CardsAmountChangeListener;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.usermanagement.IUser;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class PlayerManagement implements IPlayerManagement {
    private final ICityManagement cityManagement;

    /**
     * Listener for players cards changes.
     */
    @Setter
    private CardsAmountChangeListener cardsAmountChangeListener;

    @Inject
    public PlayerManagement(ICityManagement cityManagement) {
        this.cityManagement = cityManagement;
    }

    /**
     * Draws a player card for the specified user in the given lobby.
     * <p>
     * This method retrieves the player associated with the given user in the specified lobby,
     * and then calls the overloaded method to draw a player card for that player.
     *
     * @param lobbyCode the code of the lobby in which the game is happening
     * @param user      the user for whom the player card is to be drawn
     * @return the drawn player card as a data transfer object (DTO)
     * @throws PlayerManagementException if the player is not found for the given user
     */
    public ICardDTO drawPlayerCard(String lobbyCode, IUser user) throws PlayerManagementException {
        IPlayer player = GameStore.getInstance()
                                  .getGame(lobbyCode)
                                  .getPlayers()
                                  .stream()
                                  .filter(p -> Objects.equals(
                                          p.getUser()
                                           .getUsername(), user.getUsername()
                                  ))
                                  .findFirst()
                                  .orElseThrow(() -> new PlayerManagementException("Player not found for the given user"));
        return drawPlayerCard(lobbyCode, player);
    }

    /**
     * Draws a player card for the specified player in the given lobby.
     * <p>
     * This method retrieves a card from the player's card draw pile. If the drawn card is an EpidemicCard,
     * it increases the infection counter, draws the bottom infection card, and infects the city with the plague.
     * The EpidemicCard is then added to the player card discard pile. If the drawn card is not an EpidemicCard,
     * it is added to the player's hand. The method also updates the game state to reflect the number of cards drawn.
     *
     * @param lobbyCode the code of the lobby in which the game is happening
     * @param player    the player for whom the card is to be drawn
     * @return the drawn player card as a data transfer object (DTO)
     * @throws PlayerManagementException if there is an issue with drawing the card
     */
    public ICardDTO drawPlayerCard(String lobbyCode, IPlayer player) throws PlayerManagementException {
        IGame game = GameStore.getInstance()
                              .getGame(lobbyCode);

        ICard card = getCard(game, player);

        if (card instanceof EpidemicCard) {
            InfectionCard infectionCard = drawBottomInfectionCard(game);
            cityManagement.infectCityWithOwnPlague(game, infectionCard, 3);

            game.setInfectionCounter(game.getInfectionCounter() + 1);
            game.getPlayerCardDiscardPile()
                .add(card);
            shuffleInfectionCardsFromDrawPile(game);
        } else {
            addCard(
                    lobbyCode,
                    player.getUser()
                          .getUsername(),
                    card
            );
        }
        if (game.getState() instanceof DrawCardState drawCardState) {
            drawCardState.increaseCardsDrawn(game);
        }
        return CardMapper.toDTO(card);
    }

    /**
     * Retrieves a card from the player's card draw pile.
     * <p>
     * This method checks if it is the player's turn to draw a card by verifying the current game state and the current player.
     * If the conditions are met, it retrieves and removes the top card from the player's card draw pile.
     * If the draw pile is empty, it sets the game state to end the game and throws an exception.
     *
     * @param game   the game instance from which the card is to be drawn
     * @param player the player who is drawing the card
     * @return the drawn card
     * @throws PlayerManagementException if it is not the player's turn to draw a card or if the draw pile is empty
     */
    private ICard getCard(IGame game, IPlayer player) throws PlayerManagementException {
        if (!player.equals(game.getPlayers()
                               .get(game.getCurrentPlayerIndex())) || !(game.getState() instanceof DrawCardState) && !(game.getState() instanceof StartState)) {
            throw new PlayerManagementException("It is not the player's turn to draw a card");
        }

        List<ICard> playerCardDrawPile = game.getPlayerCardDrawPile();

        if (playerCardDrawPile.isEmpty()) {
            game.setState(new EndGameState(false));
            throw new PlayerManagementException("Player card draw pile is empty");
        }

        return playerCardDrawPile.remove(0);
    }

    /**
     * Moves a player to a specified city in the game.
     * <p>
     * This method retrieves the player associated with the given user in the specified lobby,
     * and then calls the overloaded method to move the player to the specified city.
     *
     * @param lobbyCode the code of the lobby in which the game is happening
     * @param player    the player  to be moved
     * @param cityName  the city to which the player will be moved
     * @throws PlayerManagementException if the player is not found for the given user
     */
    public void setStartingPosition(
            String lobbyCode,
            CityName cityName,
            IPlayer player
    ) throws PlayerManagementException {
        IGame game = GameStore.getInstance()
                              .getGame(lobbyCode);

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
            ICity city = game.getCityRepository()
                             .getCitiesByNames(cityName)
                             .get(0);
            player.setCurrentPosition(city);
        } else {
            throw new PlayerManagementException(
                    "Keine valide Stadt ausgewählt! Du musst eine Stadt die du auf der Hand hast auswählen!");
        }
    }

    /**
     * Adds a card to a player's hand in a specified lobby.
     *
     * @param lobbyId  the ID of the lobby
     * @param username the username of the player
     * @param card     the card to be added
     */
    public void addCard(String lobbyId, String username, ICard card) {
        IGame game = GameStore.getInstance()
                              .getGame(lobbyId);
        IPlayer player = game.getPlayer(username);

        player.getCards()
              .add(card);

        if (player.getCards()
                  .size() > 7 && cardsAmountChangeListener != null) {
            cardsAmountChangeListener.onCardsAmountChanged(
                    lobbyId,
                    username,
                    CardMapper.toMixedCardDTOList(player.getCards())
            );
        }
    }

    /**
     * Discards a card for a player in a specified lobby.
     *
     * @param lobbyCode the code of the lobby
     * @param username  the username of the player
     * @param cardId    the ID of the card to be discarded
     * @throws PlayerManagementException if an error occurs while discarding the card
     */
    public void discardPlayerCard(String lobbyCode, String username, Integer cardId) throws PlayerManagementException {
        discardPlayerCards(lobbyCode, username, List.of(cardId));
    }

    /**
     * Discards multiple cards for a player in a specified lobby.
     *
     * @param lobbyCode the code of the lobby
     * @param username  the username of the player
     * @param cardIds   the list of IDs of the cards to be discarded
     * @throws PlayerManagementException if an error occurs while discarding the cards
     */
    public void discardPlayerCards(
            String lobbyCode,
            String username,
            List<Integer> cardIds
    ) throws PlayerManagementException {
        IGame game = GameStore.getInstance()
                              .getGame(lobbyCode);
        IPlayer player = game.getPlayer(username);

        for (Integer cardId : cardIds) {
            ICard card = player.getCards()
                               .stream()
                               .filter(c -> Objects.equals(c.getId(), cardId))
                               .findFirst()
                               .orElseThrow(() -> new PlayerManagementException("Card not found"));

            player.getCards()
                  .remove(card);
            game.getPlayerCardDiscardPile()
                .add(card);
        }
    }

    /**
     * Retrieves a specific card from a player's hand.
     * <p>
     * This method retrieves the game instance using the provided lobby ID, then finds the player by their name.
     * It then searches the player's hand for a card with the specified card ID and returns it.
     * If the card is not found, it returns null.
     *
     * @param lobbyId    the ID of the lobby in which the game is happening
     * @param playerName the name of the player whose card is to be retrieved
     * @param cardId     the ID of the card to be retrieved
     * @return the card with the specified ID, or null if not found
     * @throws PlayerManagementException if the player is not found
     */
    public ICard getCard(String lobbyId, String playerName, int cardId) throws PlayerManagementException {
        IGame game = GameStore.getInstance()
                              .getGame(lobbyId);
        IPlayer player = getPlayer(game, playerName);
        return player.getCards()
                     .stream()
                     .filter(c -> Objects.equals(c.getId(), cardId))
                     .findFirst()
                     .orElse(null);
    }

    /**
     * Retrieves a player from the game by their username.
     * <p>
     * This method searches the list of players in the game for a player with the specified username.
     * If the player is found, it is returned. If the player is not found, a PlayerManagementException is thrown.
     *
     * @param game       the game instance from which the player is to be retrieved
     * @param playerName the username of the player to be retrieved
     * @return the player with the specified username
     * @throws PlayerManagementException if the player is not found
     */
    private IPlayer getPlayer(IGame game, String playerName) throws PlayerManagementException {
        return game.getPlayers()
                   .stream()
                   .filter(p -> p.getUser()
                                 .getUsername()
                                 .equals(playerName))
                   .findFirst()
                   .orElseThrow(() -> new PlayerManagementException("Player not found"));
    }

    /**
     * Sets the current position of a player in the game.
     * <p>
     * This method retrieves the game instance using the provided lobby ID, then finds the player by their name.
     * It then retrieves the city with the specified city ID and sets it as the player's current position.
     *
     * @param lobbyId    the ID of the lobby in which the game is happening
     * @param playerName the name of the player whose position is to be set
     * @param cityId     the ID of the city to which the player will be moved
     * @throws PlayerManagementException if the player is not found
     */
    public void setPlayerLocation(String lobbyId, String playerName, int cityId) throws PlayerManagementException {
        IGame game = GameStore.getInstance()
                              .getGame(lobbyId);
        IPlayer player = getPlayer(game, playerName);
        ICity city = cityManagement.getCity(lobbyId, cityId);
        player.setCurrentPosition(city);
    }

    /**
     * Shuffles the infection cards from the discard pile back into the draw pile.
     *
     * @param game The game from which the infection cards are to be shuffled
     */
    public void shuffleInfectionCardsFromDrawPile(IGame game) {
        List<InfectionCard> infectionCardsDrawPile = game.getInfectionCardDrawPile();
        List<InfectionCard> infectionCardsDiscardPile = game.getInfectionCardDiscardPile();
        Collections.shuffle(infectionCardsDiscardPile);
        infectionCardsDrawPile.addAll(0, infectionCardsDiscardPile);
        game.setInfectionCardDrawPile(infectionCardsDrawPile);
        infectionCardsDiscardPile.clear();
    }

    /**
     * Draws an infection card from the bottom of the deck.
     *
     * @return The drawn infection card, or null if no card can be drawn
     */
    public InfectionCard drawBottomInfectionCard(IGame game) throws IllegalStateException {
        List<InfectionCard> infectionCardDrawPile = game.getInfectionCardDrawPile();

        if (infectionCardDrawPile.isEmpty()) {
            throw new IllegalStateException("Infection card draw pile is empty");
        }

        return infectionCardDrawPile.remove(infectionCardDrawPile.size() - 1);
    }
}