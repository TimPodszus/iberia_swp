package de.uol.swp.server.cards.management;

import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.AnotherDayEventCard;
import de.uol.swp.server.cards.data.eventcards.EventCard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.PlayerTurnState;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Setter
public class CardManagement extends AbstractManagement implements ICardManagement {
    private static final Logger LOG = LogManager.getLogger(CardManagement.class);
    private IGame game;

    @Override
    public void playCard(String lobbyId, String username, int cardId) {
        game = super.getGame(lobbyId);
        if (isCardPlayable(cardId, username)) {
            ICard card = game.getPlayer(username)
                             .playCard(cardId);
            if (card instanceof EventCard eventCard) {
                LOG.debug("[LobbyId: {}] Card is Event Card and will be played directly", game.getGameId());
                setGameInEventCardState(game, eventCard);
                eventCard.execute(lobbyId, username);
            }
        }
    }

    /**
     * Checks if the card is playable.
     *
     * @param cardId   the id of the card to check
     * @param username the username of the player
     * @return true if the card is playable, false otherwise
     */
    boolean isCardPlayable(int cardId, String username) {
        ICard playedCard = game.getPlayer(username)
                               .getCards()
                               .stream()
                               .filter(card -> card.getId() == cardId)
                               .findFirst()
                               .orElseThrow(() -> {
                                   LOG.error(
                                           "[LobbyId: {}] Card with id {} not found in player's hand",
                                           game.getGameId(),
                                           cardId
                                   );
                                   return new IllegalArgumentException("Card with id " + cardId + " not found in player's hand");
                               });

        if (playedCard instanceof AnotherDayEventCard) {
            return isAnotherDayEventCardPlayable();
        } else {
            return true;
        }
    }

    /**
     * Checks if the AnotherDayEventCard is playable.
     *
     * @return true if the card is playable, false otherwise
     */
    boolean isAnotherDayEventCardPlayable() {
        if (game.getState() instanceof PlayerTurnState) {
            return true;
        } else {
            LOG.error(
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
