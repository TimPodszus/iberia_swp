package de.uol.swp.server.cards.management;

import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.*;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.InfectionState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForPositioning;
import de.uol.swp.server.player.data.IPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardManagement extends AbstractManagement implements ICardManagement {
    private static final Logger LOG = LogManager.getLogger(CardManagement.class);

    @Override
    public void playCard(String lobbyId, String username, int cardId) throws CardNotPlayableException {
        IGame game = super.getGame(lobbyId);
        if (isCardPlayable(game, cardId, username)) {
            ICard card = game.getPlayer(username)
                             .playCard(cardId);
            game.getPlayerCardDiscardPile()
                .add(card);
            if (card instanceof EventCard eventCard) {
                playEventCard(game, username, eventCard);
            }
        } else {
            throw new CardNotPlayableException();
        }
    }

    @Override
    public void playSecondChanceCard(String lobbyId, String username) throws CardNotFoundException {
        IGame game = super.getGame(lobbyId);
        ICity currentPosition = game.getPlayer(username)
                                    .getCurrentPosition();
        List<ICard> discardPile = game.getPlayerCardDiscardPile();

        Optional<CityCard> cardForPlayersHand = discardPile.stream()
                                        .filter(card -> card instanceof CityCard cityCard && cityCard.getCity().equals(currentPosition))
                                        .map(CityCard.class::cast)
                                        .findFirst();

        if (cardForPlayersHand.isPresent()) {
            game.getPlayer(username)
                .addCard(cardForPlayersHand.get());
            discardPile.remove(cardForPlayersHand.get());
            game.setState(game.getPreviousState());
            LOG.debug("[LobbyId: {}] CityCard added to player's hand", game.getGameId());
        } else {
            game.setState(game.getPreviousState());
            LOG.debug("[LobbyId: {}] CityCard for city {} not found in discard pile for player {}",
                    currentPosition.getId(), game.getGameId(),
                    username);
            throw new CardNotFoundException("CityCard not found in discard pile");
        }
    }

    @Override
    public void returnLastPlayedCard(String lobbyId, String username) {
        IGame game = super.getGame(lobbyId);
        ICard cardToReturn = game.getPlayerCardDiscardPile()
                                 .get(game.getPlayerCardDiscardPile().size() - 1);

        game.getPlayerCardDiscardPile()
            .remove(cardToReturn);
        game.getPlayer(username).addCard(cardToReturn);

        LOG.debug("[LobbyId: {}] returned last played card to player's hand", game.getGameId());
    }

    /**
     * Plays the event card.
     *
     * @param game      the game instance
     * @param username  the username of the player, who plays the card
     * @param eventCard the event card to play
     */
    private void playEventCard(IGame game, String username, EventCard eventCard) {
        LOG.debug("[LobbyId: {}] Card is Event Card and will be played directly", game.getGameId());
        setGameInEventCardState(game, eventCard);

        if (eventCard instanceof StateMobilizationEventCard stateMobilizationEventCard) {
            stateMobilizationEventCard.setPlayersToMove(new ArrayList<>(game.getPlayers()));
        }

        eventCard.execute(game.getGameId(), username);
    }

    /**
     * Checks if the card is playable.
     *
     * @param game     the game instance to check
     * @param cardId   the id of the card to check
     * @param username the username of the player
     * @return true if the card is playable, false otherwise
     */
    public boolean isCardPlayable(IGame game, int cardId, String username) {
        IPlayer player = game.getPlayer(username);
        ICard playedCard = player.getCard(cardId);
        if (playedCard == null) {
            LOG.warn("[LobbyId: {}] Card with id {} not found in player's hand", game.getGameId(), cardId);
            return false;
        }
        if (game.getState() instanceof WaitForPositioning) {
            LOG.warn("[LobbyId: {}] Card can't be played during WaitForPositioning state", game.getGameId());
            return false;
        }
        if (playedCard instanceof AnotherDayEventCard) {
            return isAnotherDayEventCardPlayable(game);
        } else if (playedCard instanceof TreatWaterEventCard) {
            return isTreatWaterEventCardPlayable(game);
        } else {
            return isStateCorrect(game);
        }
    }

    /**
     * Checks if the AnotherDayEventCard is playable.
     *
     * @return true if the card is playable, false otherwise
     */
    private boolean isAnotherDayEventCardPlayable(IGame game) {
        if (game.getState() instanceof PlayerTurnState) {
            return true;
        } else {
            LOG.warn(
                    "[LobbyId: {}] AnotherDayEventCard can only be played during the PlayerTurnState",
                    game.getGameId()
            );
            return false;
        }
    }

    boolean isTreatWaterEventCardPlayable(IGame game) {
        if (isStateCorrect(game) && game.getWaterTreatmentsLeft() > 0) {
            return true;
        } else {
            LOG.warn("[LobbyId: {}] TreatWaterEventCard can not be played", game.getGameId());
            return false;
        }
    }

    public boolean isStateCorrect(IGame game) {
        if (game.getState() instanceof PlayerTurnState || game.getState() instanceof InfectionState || game.getState() instanceof DrawCardState) {
            return true;
        } else {

            LOG.warn("[LobbyId: {}] Card is not playable in the current state", game.getGameId());
            return false;
        }
    }

    /**
     * Sets the game state to EventState.
     *
     * @param game the game instance to update
     */
    private void setGameInEventCardState(IGame game, EventCard eventCard) {
        LOG.debug("[LobbyId: {}] Setting game in EventState", game.getGameId());
        game.setState(new EventState(eventCard));
    }

    @Override
    public <T extends ICard> List<T> getCardsFromPlayerDiscardPile(String lobbyId, Class<T> type) {
        LOG.debug(
                "[LobbyId: {}] Getting cards with type {} from player card discard pile",
                lobbyId,
                type.getSimpleName()
        );
        IGame game = super.getGame(lobbyId);
        List<ICard> playerCardDiscardPile = game.getPlayerCardDiscardPile();
        List<T> cards = new ArrayList<>();
        for (ICard discardedCard : playerCardDiscardPile) {
            if (type.isInstance(discardedCard)) {
                cards.add(type.cast(discardedCard));
            }
        }
        LOG.info("[LobbyId: {}] Returning cards with specified type from player card discard pile", lobbyId);
        return cards;
    }

    @Override
    public void getCardForPlayer(
            String lobbyId, String username, int cardId
    ) throws IllegalGameStateException, CardNotFoundException {
        IGame game = super.getGame(lobbyId);
        if (!(game.getState() instanceof EventState eventState && eventState.getEventCard() instanceof ForTheGoodCauseEventCard)) {
            LOG.error("[LobbyId: {}] Game is not in correct state to get card for player", lobbyId);
            throw new IllegalGameStateException("Game is not in correct state to get card for player");
        }

        LOG.debug("[LobbyId: {}] Getting card {} for {}", lobbyId, cardId, username);
        List<EventCard> cards = this.getCardsFromPlayerDiscardPile(lobbyId, EventCard.class);
        ICard card = cards.stream()
                          .filter(eventCard -> eventCard.getId() == cardId)
                          .findFirst()
                          .orElseThrow(() -> {
                              LOG.error("[LobbyId: {}] Card not found in player card discard pile", lobbyId);
                              return new CardNotFoundException("Card not found in player card discard pile");
                          });

        game.getPlayerCardDiscardPile()
            .remove(card);

        game.getPlayer(username)
            .addCard(card);
        game.setState(game.getPreviousState());
        LOG.info(
                "[LobbyId: {}] Card from player discard pile added to player's hand. Eventcard is handled, game will resume in last active state",
                lobbyId
        );
    }
}
