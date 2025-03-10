package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;

public class TreatExtraPlagueState implements IGameState{
    @Override
    public StateType getStateType() {
        return StateType.TREAT_EXTRA_PLAGUE_STATE;
    }
}
