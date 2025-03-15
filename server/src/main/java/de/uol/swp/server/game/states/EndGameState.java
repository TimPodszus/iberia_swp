package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndGameState implements IGameState {
    private final boolean isVictory;

    public EndGameState(boolean isVictory) {
        this.isVictory = isVictory;
    }

    public StateType getStateType() {
        return StateType.END_GAME_STATE;
    }
}