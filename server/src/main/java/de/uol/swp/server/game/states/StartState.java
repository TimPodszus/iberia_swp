package de.uol.swp.server.game.states;

import de.uol.swp.common.game.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.player.Player;

/**
 * Represents the initial state of the game where setup actions are performed.
 * This state is responsible for initializing the game setup and transitioning
 * the game state to WaitForPositioning, which typically handles player positioning at the start of the game.
 */
public class StartState implements IGameState {

    /**
     * Handles the initialization action when the game starts.
     * This method initializes the game by calling the initializeGame method on the controller
     * and then transitions the game state to WaitForPositioning to proceed with player positioning.
     *
     * @param controller the game controller that manages state transitions and interactions within the game
     * @param action the action performed by the player that triggers the initialization (usually at game start)
     * @param player the player involved in the action; typically not used directly in the initialization phase
     */
    public void handleAction(GameController controller, Action action, Player player) {
        controller.initializeGame();
        controller.setState(new WaitForPositioning());
    }
}
