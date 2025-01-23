package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;

public class EventState implements IGameState {
    public StateType getStateType() {
        return StateType.EVENT_STATE;
    }
}
