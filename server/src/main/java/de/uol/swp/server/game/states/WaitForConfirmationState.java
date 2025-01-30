package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import de.uol.swp.common.player.IPlayerDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaitForConfirmationState implements IGameState{
    IPlayerDTO waitingForPlayer;


    @Override
    public StateType getStateType() {
        return StateType.WAIT_FOR_CONFIRMATION_STATE;
    }
}
