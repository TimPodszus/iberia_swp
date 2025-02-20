package de.uol.swp.server.cards.management;

import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.AnotherDayEventCard;
import de.uol.swp.server.cards.data.eventcards.EventCard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.player.data.IPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CardManagement extends AbstractManagement implements ICardManagement {
    private static final Logger LOG = LogManager.getLogger(CardManagement.class);

    @Override
    public void playCard(String lobbyId, String username, int cardId) {
        IGame game = super.getGame(lobbyId);
        if (isCardPlayable(game, cardId, username)) {
            ICard card = game.getPlayer(username)
                             .playCard(cardId);
            if (card instanceof EventCard eventCard) {
                LOG.debug("[LobbyId: {}] Card is Event Card and will be played directly", game.getGameId());
                setGameInEventCardState(game, eventCard);
                eventCard.execute(lobbyId, username);
            }
        } else {
            LOG.warn("[LobbyId: {}] Card with id {} is not playable", game.getGameId(), cardId);
            //Todo: #202 - Was passiert mit serverseitigen Exceptions?
        }
    }

    /**
     * Checks if the card is playable.
     *
     * @param game     the game instance to check
     * @param cardId   the id of the card to check
     * @param username the username of the player
     * @return true if the card is playable, false otherwise
     */
    boolean isCardPlayable(IGame game, int cardId, String username) {
        IPlayer player = game.getPlayer(username);
        ICard playedCard = player.getCard(cardId);
        if (playedCard == null) {
            LOG.warn("[LobbyId: {}] Card with id {} not found in player's hand", game.getGameId(), cardId);
            return false;
        }
        if (playedCard instanceof AnotherDayEventCard) {
            return isAnotherDayEventCardPlayable(game);
        } else {
            return true;
        }
    }

    /**
     * Checks if the AnotherDayEventCard is playable.
     *
     * @return true if the card is playable, false otherwise
     */
    boolean isAnotherDayEventCardPlayable(IGame game) {
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
