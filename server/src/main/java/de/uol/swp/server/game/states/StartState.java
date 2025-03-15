package de.uol.swp.server.game.states;


import de.uol.swp.common.game.StateType;

/**
 * Represents the initial state of the game where essential setup actions are performed.
 * This state is specifically responsible for initializing all game components and settings,
 * and it ensures that the game is ready for player interactions by transitioning to the
 * WaitForPositioning state, which manages initial player placement on the game board.
 */
public class StartState implements IGameState {
    public StateType getStateType() {
        return StateType.START_STATE;
    }
}