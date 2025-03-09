package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import de.uol.swp.server.connection.data.IConnection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the state of building extra train tracks during a player's turn.
 * <p>
 * This state holds the list of connections that can be built by the player.
 */
@Setter
@Getter
public class BuildExtraTrainTrackState extends PlayerTurnState {
    private List<IConnection> connections;

    public BuildExtraTrainTrackState(List<IConnection> connections, int actionsRemaining) {
        super();
        this.connections = connections;
        this.actionsRemaining = actionsRemaining;
    }

    /**
     * Retrieves the type of this state.
     *
     * @return The state type, which is BUILD_EXTRA_TRAIN_TRACK_STATE.
     */
    @Override
    public StateType getStateType() {
        return StateType.BUILD_EXTRA_TRAIN_TRACK_STATE;
    }
}
