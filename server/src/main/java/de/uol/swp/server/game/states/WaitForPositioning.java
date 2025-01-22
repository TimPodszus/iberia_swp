package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the game state where players are setting their initial positions at the start of the game.
 * This state manages the positioning of players based on their chosen locations and ensures that all players
 * are positioned before moving to the next game phase.
 */
@Getter
@Setter
public class WaitForPositioning implements IGameState {
    private int positionedPlayersCount = 0;

    public StateType getStateType() {
        return StateType.WAIT_FOR_POSITIONING_STATE;
    }
}
