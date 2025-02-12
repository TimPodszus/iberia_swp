package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import de.uol.swp.server.connection.data.IConnection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class BuildExtraTrainTrackState extends PlayerTurnState {
    private List<IConnection> connections;

    @Override
    public StateType getStateType() {
        return StateType.BUILD_EXTRA_TRAIN_TRACK_STATE;
    }
}
