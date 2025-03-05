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
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.role.ScientistAtTheRoyalAcademy;
import de.uol.swp.server.usermanagement.IUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class PlayerManagement extends AbstractManagement implements IPlayerManagement{
    static final Logger LOG = LogManager.getLogger(PlayerManagement.class);
    private final ICityManagement cityManagement;

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
        IPlayer player = getGame(lobbyCode)
                                  .getPlayers()
                                  .stream()
                                  .filter(p -> Objects.equals(
                                          p.getUser()
                                           .getUsername(), user.getUsername()
                                  ))
                                  .findFirst()
                                  .orElseThrow(() -> new PlayerManagementException("Player not found for the given user"));
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
            LOG.debug("[LobbyID: {}] Epidemic card drawn. Infection counter increased to {}", lobbyCode, game.getInfectionCounter());
        } else {
            addCard(player, card);
        }
        if (game.getState() instanceof DrawCardState drawCardState) {
            drawCardState.increaseCardsDrawn(game);
            LOG.debug("[LobbyID: {}] Cards drawn increased for player {}", lobbyCode, player.getUser().getUsername());
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
            LOG.error("[LobbyID: {}] Failed to draw card. It is not the player's turn or game is not in a state that allows drawing cards", game.getGameId());
            throw new PlayerManagementException("It is not the player's turn to draw a card");
        }

        List<ICard> playerCardDrawPile = game.getPlayerCardDrawPile();

        if (playerCardDrawPile.isEmpty()) {
            game.setState(new EndGameState(false));
            LOG.error("[LobbyID: {}] Player card draw pile is empty. Game ended", game.getGameId());
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
     * Adds a card to the player's hand.
     * <p>
     * This method adds the specified card to the list of cards held by the player.
     *
     * @param player the player to whom the card is to be added
     * @param card   the card to be added to the player's hand
     */
    public void addCard(IPlayer player, ICard card) {
        player.getCards()
              .add(card);
    }

    /**
     * Discards a single card from the player's hand.
     * <p>
     * This method removes the specified card from the player's hand and adds it to the player card discard pile in the game.
     *
     * @param lobbyCode the code of the lobby in which the game is happening
     * @param player    the player from whose hand the card is to be discarded
     * @param card      the card to be discarded
     */
    public void discardCard(String lobbyCode, IPlayer player, ICard card) {
        discardCards(lobbyCode, player, List.of(card));
        LOG.debug("[LobbyID: {}] Player {} discarded card {}", lobbyCode, player.getUser().getUsername(), card.getId());
    }

    /**
     * Discards multiple cards from the player's hand.
     * <p>
     * This method removes the specified cards from the player's hand and adds them to the player card discard pile in the game.
     *
     * @param lobbyCode the code of the lobby in which the game is happening
     * @param player    the player from whose hand the cards are to be discarded
     * @param cards     the list of cards to be discarded
     */
    public void discardCards(String lobbyCode, IPlayer player, List<? extends ICard> cards) {
        IGame game = GameStore.getInstance()
                              .getGame(lobbyCode);

        for (ICard card : cards) {
            player.getCards()
                  .remove(card);
        }
        game.getPlayerCardDiscardPile()
            .addAll(cards);
        LOG.debug("[LobbyID: {}] Player {} discarded {} cards", lobbyCode, player.getUser().getUsername(), cards.size());
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
            LOG.error("[LobbyID: {}] Failed to draw infection card. Infection card draw pile is empty", game.getGameId());
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
     * @throws GameException if the player is not found for the given user or if the player is not the current player or does not have the role of ScientistAtTheRoyalAcademy
     * @throws IllegalGameStateException     if the game is not in the turn state
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
    public void sortCards(String lobbyId, IUser user, List<ICardDTO> cards) throws GameException {
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
            LOG.debug("[LobbyID: {}] Cards sorted for player {}", lobbyId, player.getUser().getUsername());
        } else {
            LOG.error("[LobbyID: {}] Invalid request to sort cards by player {}", lobbyId, user.getUsername());
            throw new GameException(
                    "It is not your turn or your role is not scientist of the royal academy");
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