package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the state in the game where it is a player's turn to take actions.
 * This state is responsible for managing the actions a player takes during their turn,
 * tracking the number of actions remaining, and transitioning to the next state based
 * on the completion of these actions.
 */
@Getter
@Setter
public class PlayerTurnState implements IGameState {
    int actionsRemaining = 4;

    public StateType getStateType() {
        return StateType.PLAYER_TURN_STATE;
    }
}
