package de.uol.swp.server.game.states;

import de.uol.swp.common.game.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.player.Player;

/**
 * Represents the state in a game where players draw cards.
 * This state manages the drawing of player cards during a game turn.
 * It transitions to the InfectionState after certain conditions are met.
 */
public class DrawCardState implements GameState {
    private int cardsDrawn = 0;

    /**
     * Handles the action of drawing cards for a player.
     * Each call to this method allows the player to draw one card and increments
     * the count of cards drawn. If a player has drawn two cards and the number of cards
     * they hold is seven or fewer, the game state transitions to InfectionState.
     *
     * @param controller the game controller managing the state transitions and other game interactions
     * @param action the action that triggered this method, typically involving card drawing
     * @param player the player who is drawing the card
     */
    public void handleAction(GameController controller, Action action, Player player) {
        controller.getCurrentTurn()
                  .drawPlayerCard();
        cardsDrawn++;
        if (player.getCards()
                  .size() <= 7 && cardsDrawn == 2) {
            controller.setState(new InfectionState());
        }
    }
}
