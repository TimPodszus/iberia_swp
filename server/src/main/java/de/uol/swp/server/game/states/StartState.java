package de.uol.swp.server.game.states;

import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.Player;

/**
 * Represents the initial state of the game where essential setup actions are performed.
 * This state is specifically responsible for initializing all game components and settings,
 * and it ensures that the game is ready for player interactions by transitioning to the
 * WaitForPositioning state, which manages initial player placement on the game board.
 */
public class StartState implements IGameState {

    /**
     * Executes the initial setup actions for the game when the game instance is first started.
     * This method calls the initializeGame method on the game object, setting up the game environment
     * based on the selected difficulty and other initial parameters. After initialization, it transitions
     * the game state to WaitForPositioning, which allows players to place their pawns on the designated
     * starting cities. This method is typically the first action executed in the lifecycle of a game session.
     *
     * @param game   the game context in which the setup is executed
     * @param player the player initiating the setup; typically this parameter is not used as setup
     *               is generally independent of specific player actions
     */
    public void handleAction(IGame game, Player player) {
        // TODO issue zur überarbeitung der states
    }
}
