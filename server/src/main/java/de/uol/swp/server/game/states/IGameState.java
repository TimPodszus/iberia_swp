package de.uol.swp.server.game.states;

import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.player.Player;

/**
 * Represents a state within the game state machine.
 * This interface defines a common protocol for game states to handle actions during the game.
 * Implementing classes will represent specific states of the game, each with their own logic
 * for handling actions based on the current game context.
 */
public interface IGameState {

    /**
     * Handles the action taken by a player within the context of this specific game state.
     * Implementations of this method in concrete state classes will process the action
     * according to the rules and logic specific to that state, potentially triggering
     * transitions to other states within the game's state machine.
     *
     * @param player the player who performed the action
     */
    void handleAction(Game game, Player player);
}
