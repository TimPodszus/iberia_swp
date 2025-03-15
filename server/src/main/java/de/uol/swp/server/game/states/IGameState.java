package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;

/**
 * Represents a state within the game state machine.
 * This interface defines a common protocol for game states to handle actions during the game.
 * Implementing classes will represent specific states of the game, each with their own logic
 * for handling actions based on the current game context.
 */
public interface IGameState {
    StateType getStateType();
}