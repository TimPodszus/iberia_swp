package de.uol.swp.server.cards.management;

import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.EventCard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.EventState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CardManagement extends AbstractManagement implements ICardManagement {
    private static final Logger LOG = LogManager.getLogger(CardManagement.class);

    @Override
    public void playCard(String lobbyId, String username, int cardId) {
        IGame game = super.getGame(lobbyId);

        if (!game.getPlayer(username).equals(game.getCurrentPlayer())) {
            LOG.error("[LobbyId: {}] Player is not current player", game.getGameId());
            //TODO think, wether to throw an exception or not
            return;
        }

        ICard card = game.getPlayer(username).playCard(cardId);
        if (card instanceof EventCard eventCard) {
            LOG.debug("[LobbyId: {}] Card is Event Card and will be played directly", game.getGameId());
            setGameInEventCardState(game);
            eventCard.execute(lobbyId, username);
        }
    }

    /**
     * Sets the game state to EventState.
     *
     * @param game the game instance to update
     */
    private void setGameInEventCardState(IGame game) {
        LOG.debug("[LobbyId: {}] Setting game in EventState", game.getGameId());
        game.setState(new EventState());
    }
}
