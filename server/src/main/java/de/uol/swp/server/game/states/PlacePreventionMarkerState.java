package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;

public class PlacePreventionMarkerState implements IGameState{
    @Override
    public StateType getStateType() {
        return StateType.PLACE_PREVENTION_MARKER_STATE;
    }
}
