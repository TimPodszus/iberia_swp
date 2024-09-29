package de.uol.swp.server.game.states;

import de.uol.swp.common.game.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.player.Player;

/**
 * Represents the state in a game where it is a player's turn to take actions.
 * This state manages the actions taken by a player during their turn and
 * transitions to the next state based on the actions' outcomes.
 */
public class PlayerTurnState implements GameState {

    /**
     * Handles player actions during their turn.
     * This method processes any action taken by the player and checks if the player's
     * turn is over. If the turn is over, it transitions the game state to the
     * DrawCardState to proceed with drawing cards.
     *
     * @param controller the game controller that manages state transitions and other game interactions
     * @param action the player's action that needs to be processed
     * @param player the player who is taking the action
     */
    public void handleAction(GameController controller, Action action, Player player) {
        controller.getCurrentTurn()
                  .processAction(action);

        if (controller.getCurrentTurn()
                      .isTurnOver()) {
            controller.setState(new DrawCardState());
        }
    }
}
