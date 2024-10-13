package de.uol.swp.server.game.states;

import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.Player;

/**
 * Represents the state in a game where players draw cards.
 * This state is responsible for managing the action of drawing player cards during a game turn.
 * It ensures that each player draws two cards and then transitions the game state to InfectionState,
 * provided the player holds seven or fewer cards.
 */
public class DrawCardState implements IGameState {
    private int cardsDrawn = 0;

    /**
     * Processes the card drawing action for a player in the game. Each call allows the player
     * to draw one card from the player deck. Once a player has drawn two cards, this method checks
     * the total number of cards held by the player. If the player holds seven or fewer cards,
     * the game state is transitioned to InfectionState.
     *
     * @param game the game in which this action is being processed
     * @param player the player who is drawing the card
     */
    public void handleAction(IGame game, Player player) {
        game.getGameManagement().drawPlayerCard();
        cardsDrawn++;
        if (cardsDrawn == 2 && player.getCards().size() <= 7) {
            game.setState(new InfectionState());
        }
    }
}
