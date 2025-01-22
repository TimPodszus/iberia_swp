package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;

public class EndGameState implements IGameState {

    public StateType getStateType() {
        return StateType.END_GAME_STATE;
    }
}