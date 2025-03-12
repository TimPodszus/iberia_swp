package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import de.uol.swp.server.connection.data.IConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class BuildExtraTrainTrackStateTest {

    @Mock
    private IConnection connection1;

    @Mock
    private IConnection connection2;

    private BuildExtraTrainTrackState state;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        state = new BuildExtraTrainTrackState(List.of(connection1, connection2), 4);
    }

    @Test
    void testGetStateType() {
        assertEquals(StateType.BUILD_EXTRA_TRAIN_TRACK_STATE, state.getStateType());
    }

    @Test
    void testGetConnections() {
        List<IConnection> connections = state.getConnections();
        assertEquals(2, connections.size());
        assertEquals(connection1, connections.get(0));
        assertEquals(connection2, connections.get(1));
    }

    @Test
    void testSetConnections() {
        IConnection connection3 = mock(IConnection.class);
        state.setConnections(List.of(connection3));

        assertEquals(1, state.getConnections().size());
        assertEquals(connection3, state.getConnections().get(0));
    }
}
