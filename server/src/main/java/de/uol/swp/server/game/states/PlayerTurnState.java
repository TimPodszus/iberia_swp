package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import de.uol.swp.server.game.data.IGame;
import lombok.Getter;

/**
 * Represents the state in the game where it is a player's turn to take actions.
 * This state is responsible for managing the actions a player takes during their turn,
 * tracking the number of actions remaining, and transitioning to the next state based
 * on the completion of these actions.
 */
@Getter
public class PlayerTurnState implements IGameState {
    int actionsRemaining = 4;
    public StateType getStateType() {
        return StateType.PLAYER_TURN_STATE;
    }

    public void reduceActionsRemaining(IGame game) {
        actionsRemaining--;
        if (actionsRemaining == 0) {
            game.setState(new DrawCardState());
        }
    }
}