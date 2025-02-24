package de.uol.swp.server.cards.management;

import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.AnotherDayEventCard;
import de.uol.swp.server.cards.data.eventcards.EventCard;
import de.uol.swp.server.cards.data.eventcards.StateMobilizationEventCard;
import de.uol.swp.server.cards.data.eventcards.TreatWaterEventCard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.InfectionState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.player.data.IPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class CardManagement extends AbstractManagement implements ICardManagement {
    private static final Logger LOG = LogManager.getLogger(CardManagement.class);

    @Override
    public void playCard(String lobbyId, String username, int cardId) {
        IGame game = super.getGame(lobbyId);
        if (isCardPlayable(game, cardId, username)) {
            ICard card = game.getPlayer(username)
                             .playCard(cardId);
            if (card instanceof EventCard eventCard) {
                playEventCard(game, username, eventCard);
        }
        }
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
        } else {
            LOG.warn("[LobbyId: {}] Card with id {} is not playable", game.getGameId(), eventCard.getId());
            //Todo: #202 - Was passiert mit serverseitigen Exceptions?
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
    private boolean isCardPlayable(IGame game, int cardId, String username) {
        IPlayer player = game.getPlayer(username);
        ICard playedCard = player.getCard(cardId);
        if (playedCard == null) {
            LOG.warn("[LobbyId: {}] Card with id {} not found in player's hand", game.getGameId(), cardId);
            return false;
        }
        if (playedCard instanceof AnotherDayEventCard) {
            return isAnotherDayEventCardPlayable(game);
        } else if (playedCard instanceof TreatWaterEventCard) {
            return isTreatWaterEventCardPlayable(game);
        } else if (isStateCorrect(game)) {
            return true;
        } else {
            LOG.warn("[LobbyId: {}] Card with id {} is not playable in the current state", game.getGameId(), cardId);
            return false;
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

    boolean isStateCorrect(IGame game) {
        return game.getState() instanceof PlayerTurnState || game.getState() instanceof InfectionState || game.getState() instanceof DrawCardState;
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
}
