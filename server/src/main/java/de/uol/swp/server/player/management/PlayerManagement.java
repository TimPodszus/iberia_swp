package de.uol.swp.server.player.management;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.*;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.EpidemicCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.StartState;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.CardsAmountChangeListener;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.role.ScientistAtTheRoyalAcademy;
import de.uol.swp.server.usermanagement.IUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class PlayerManagement extends AbstractManagement implements IPlayerManagement {
    static final Logger LOG = LogManager.getLogger(PlayerManagement.class);
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
     * @throws IllegalGameStateException if the player is not found for the given user
     */
    public ICardDTO drawPlayerCard(String lobbyCode, IUser user) throws IllegalGameStateException {
        IPlayer player = getGame(lobbyCode).getPlayer(user.getUsername());
        LOG.debug("[LobbyID: {}] Player found for user {} and card drawn", lobbyCode, user.getUsername());
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
     * @throws IllegalGameStateException if it is not the player's turn to draw a card
     */
    public ICardDTO drawPlayerCard(String lobbyCode, IPlayer player) throws IllegalGameStateException {
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
            LOG.debug(
                    "[LobbyID: {}] Epidemic card drawn. Infection counter increased to {}",
                    lobbyCode,
                    game.getInfectionCounter()
            );
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
            LOG.debug(
                    "[LobbyID: {}] Cards drawn increased for player {}",
                    lobbyCode,
                    player.getUser()
                          .getUsername()
            );
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
     * @throws IllegalGameStateException if it is not the player's turn to draw a card or if the draw pile is empty
     */
    private ICard getCard(IGame game, IPlayer player) throws IllegalGameStateException {
        if (!player.equals(game.getPlayers()
                               .get(game.getCurrentPlayerIndex())) || !(game.getState() instanceof DrawCardState) && !(game.getState() instanceof StartState)) {
            LOG.error(
                    "[LobbyID: {}] Failed to draw card. It is not the player's turn or game is not in a state that allows drawing cards",
                    game.getGameId()
            );
            throw new IllegalGameStateException("It is not the player's turn to draw a card");
        }

        List<ICard> playerCardDrawPile = game.getPlayerCardDrawPile();

        if (playerCardDrawPile.isEmpty()) {
            game.setState(new EndGameState(false));
            LOG.info("[LobbyID: {}] Player card draw pile is empty. Game ended", game.getGameId());

            return null;
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
                             .getCityByName(cityName);
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
     * @throws GameException if an error occurs while discarding the card
     */
    public void discardPlayerCard(String lobbyCode, String username, Integer cardId) throws GameException {
        discardPlayerCards(lobbyCode, username, List.of(cardId));
        LOG.debug("[LobbyID: {}] Player {} discarded card {}", lobbyCode, username, cardId);
    }

    /**
     * Discards multiple cards for a player in a specified lobby.
     *
     * @param lobbyCode the code of the lobby
     * @param username  the username of the player
     * @param cardIds   the list of IDs of the cards to be discarded
     * @throws GameException if an error occurs while discarding the cards
     */
    public void discardPlayerCards(String lobbyCode, String username, List<Integer> cardIds) throws GameException {
        IGame game = getGame(lobbyCode);
        IPlayer player = game.getPlayer(username);

        for (Integer cardId : cardIds) {
            ICard card = player.getCards()
                               .stream()
                               .filter(c -> Objects.equals(c.getId(), cardId))
                               .findFirst()
                               .orElseThrow(() -> new GameException("Card not found"));

            player.getCards()
                  .remove(card);
            game.getPlayerCardDiscardPile()
                .add(card);
        }
    }

    public ICard getCard(String lobbyId, String playerName, int cardId) {
        IPlayer player = getGame(lobbyId).getPlayer(playerName);
        return player.getCards()
                     .stream()
                     .filter(c -> Objects.equals(c.getId(), cardId))
                     .findFirst()
                     .orElse(null);
    }

    public void setPlayerLocation(String lobbyId, String playerName, int cityId) {
        IPlayer player = getGame(lobbyId).getPlayer(playerName);
        ICity city = cityManagement.getCity(lobbyId, cityId);
        player.setCurrentPosition(city);
        LOG.debug("[LobbyID: {}] Player {} moved to city with ID {}", lobbyId, playerName, cityId);
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
            LOG.error(
                    "[LobbyID: {}] Failed to draw infection card. Infection card draw pile is empty",
                    game.getGameId()
            );
            throw new IllegalStateException("Infection card draw pile is empty");
        }

        InfectionCard card = infectionCardDrawPile.remove(infectionCardDrawPile.size() - 1);
        LOG.debug("[LobbyID: {}] Infection card {} drawn from bottom of the pile", game.getGameId(), card.getId());
        return card;
    }

    /**
     * Gets the cards to sort for the specified user in the given lobby.
     * This method retrieves the game instance using the provided lobby ID, then finds the player by their user.
     * It then checks if the player is the current player and has the role of ScientistAtTheRoyalAcademy.
     * If the conditions are met, it retrieves the top three cards from the player's card draw pile and returns them.
     *
     * @param lobbyId the ID of the lobby
     * @param user    the user for whom the cards are to be sorted
     * @return the top three cards from the player's card draw pile
     * @throws GameException             if the player is not found for the given user or if the player is not the current player or does not have the role of ScientistAtTheRoyalAcademy
     * @throws IllegalGameStateException if the game is not in the turn state
     */
    @Override
    public List<ICardDTO> getCardsToSort(String lobbyId, IUser user) throws GameException, IllegalGameStateException {
        IGame game = getGame(lobbyId);
        List<ICard> cards = new ArrayList<>();
        if (!(game.getState() instanceof PlayerTurnState playerTurnState)) {
            LOG.error("[LobbyID: {}] Game is not in PlayerTurnState", lobbyId);
            throw new IllegalGameStateException("The game´s current state is not playerturnstate");
        }
        IPlayer player = getPlayerByUser(user, game);
        if (player != null && player.equals(game.getCurrentPlayer()) && player.getRole() instanceof ScientistAtTheRoyalAcademy) {
            for (int i = 0; i < 3; i++) {
                if (game.getPlayerCardDrawPile()
                        .isEmpty()) {
                    break;
                }
                cards.add(game.getPlayerCardDrawPile()
                              .get(i));
            }
            playerTurnState.reduceActionsRemaining(game);
            LOG.debug(
                    "[LobbyID: {}] Top 3 cards retrieved for sorting by player {}",
                    lobbyId,
                    player.getUser()
                          .getUsername()
            );
        } else {
            LOG.error("[LobbyID: {}] Invalid request to sort cards by player {}", lobbyId, user.getUsername());
            throw new GameException("It is not your turn or your role is not scientist of the royal academy");
        }
        return CardMapper.toMixedCardDTOList(cards);
    }

    /**
     * Sorts the cards for the specified user in the given lobby.
     * This method retrieves the game instance using the provided lobby ID, then finds the player by their user.
     * It then checks if the player is the current player and has the role of ScientistAtTheRoyalAcademy.
     * If the conditions are met, it sorts the cards in the player's card draw pile according to the specified order.
     *
     * @param lobbyId the ID of the lobby
     * @param user    the user for whom the cards are to be sorted
     * @param cards   the list of cards to be sorted
     * @throws IllegalStateException if the player is not the current player or does not have the role of ScientistAtTheRoyalAcademy
     */
    @Override
    public void sortCards(String lobbyId, IUser user, List<ICardDTO> cards) throws IllegalGameStateException {
        IGame game = getGame(lobbyId);
        IPlayer player = getPlayerByUser(user, game);
        if (player != null && player.equals(game.getCurrentPlayer()) && player.getRole() instanceof ScientistAtTheRoyalAcademy) {
            List<ICard> playerCardDrawPile = game.getPlayerCardDrawPile();
            for (int i = cards.size() - 1; i >= 0; i--) {
                ICardDTO cardDTO = cards.get(i);
                ICard card = game.getPlayerCardDrawPile()
                                 .stream()
                                 .filter(c -> Objects.equals(c.getId(), cardDTO.getId()))
                                 .findFirst()
                                 .orElseThrow(() -> new IllegalStateException("Card not found"));
                playerCardDrawPile.remove(card);
                playerCardDrawPile.add(0, card);
            }
            LOG.debug(
                    "[LobbyID: {}] Cards sorted for player {}",
                    lobbyId,
                    player.getUser()
                          .getUsername()
            );
        } else {
            LOG.error("[LobbyID: {}] Invalid request to sort cards by player {}", lobbyId, user.getUsername());
            throw new IllegalGameStateException("It is not your turn or your role is not scientist of the royal academy");
        }
    }

    /**
     * Gets the Player by the User.
     *
     * @param user The User
     * @param game The Game
     * @return The Player
     */
    public IPlayer getPlayerByUser(IUser user, IGame game) {
        return game.getPlayers()
                   .stream()
                   .filter(p -> p.getUser()
                                 .equals(user))
                   .findFirst()
                   .orElse(null);
    }
}