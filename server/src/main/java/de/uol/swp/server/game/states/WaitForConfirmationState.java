package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import de.uol.swp.common.player.IPlayerDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the state where the game is waiting for a player's confirmation.
 */
@Getter
@Setter
public class WaitForConfirmationState implements IGameState {
    private final StateType stateType = StateType.WAIT_FOR_CONFIRMATION_STATE;
    /**
     * The player the game is waiting for.
     */
    IPlayerDTO waitingForPlayer;

}