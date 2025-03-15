package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import de.uol.swp.common.player.IPlayerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class WaitForConfirmationStateTest {

    private WaitForConfirmationState state;
    private IPlayerDTO mockPlayer;


    @BeforeEach
    void setUp() {
        state = new WaitForConfirmationState();
        mockPlayer = mock(IPlayerDTO.class);
    }

    @Test
    void testGetStateType() {
        assertEquals(StateType.WAIT_FOR_CONFIRMATION_STATE, state.getStateType());
    }

    @Test
    void testSetAndGetWaitingForPlayer() {
        state.setWaitingForPlayer(mockPlayer);
        assertEquals(mockPlayer, state.getWaitingForPlayer());
    }
}