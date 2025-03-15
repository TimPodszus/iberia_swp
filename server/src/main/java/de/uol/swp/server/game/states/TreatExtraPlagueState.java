package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;

/**
 * Represents the state where a player can treat an extra plague.
 * This state is part of the player's turn.
 */
public class TreatExtraPlagueState extends PlayerTurnState implements IGameState {

    /**
     * Constructs a new TreatExtraPlagueState with the specified number of actions remaining.
     *
     * @param actionsRemaining the number of actions remaining for the player
     */
    public TreatExtraPlagueState(int actionsRemaining) {
        super();
        this.actionsRemaining = actionsRemaining;
    }

    /**
     * Returns the type of this state.
     *
     * @return the state type, which is TREAT_EXTRA_PLAGUE_STATE
     */
    @Override
    public StateType getStateType() {
        return StateType.TREAT_EXTRA_PLAGUE_STATE;
    }
}