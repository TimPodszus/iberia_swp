package de.uol.swp.server.game.states;

import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.Player;

/**
 * Represents the state in the game where it is a player's turn to take actions.
 * This state is responsible for managing the actions a player takes during their turn,
 * tracking the number of actions remaining, and transitioning to the next state based
 * on the completion of these actions.
 */
public class PlayerTurnState implements IGameState {
    int actionsRemaining = 4; // Tracks the number of actions a player has left in their turn

    /**
     * Processes actions taken by the player during their turn in the game.
     * This method decrements the count of actions remaining after each action taken.
     * If the player has completed all their actions (actionsRemaining reaches zero),
     * the game state transitions to DrawCardState, where the player will proceed to
     * draw cards as the next phase of their turn.
     *
     * @param game   the game context in which the player is acting
     * @param player the player who is taking actions
     */
    public void handleAction(IGame game, Player player) {
        actionsRemaining--; // Decrement the count each time an action is processed
        if (actionsRemaining == 0) {
            game.setState(new DrawCardState()); // Transition to card drawing phase
        }
    }
}
