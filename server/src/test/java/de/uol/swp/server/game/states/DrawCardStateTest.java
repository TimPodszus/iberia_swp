package de.uol.swp.server.game.states;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.uol.swp.common.game.StateType;
import de.uol.swp.server.game.data.IGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DrawCardStateTest {

    private DrawCardState state;
    private IGame mockGame;

    @BeforeEach
    void setUp() {
        state = new DrawCardState();
        mockGame = mock(IGame.class);
    }

    @Test
    void testInitialStateType() {
        assertEquals(StateType.DRAW_CARD_STATE, state.getStateType(), "The initial state type should be DRAW_CARD_STATE");
    }

    @Test
    void testInitialCardsDrawn() {
        assertEquals(0, state.getCardsDrawn(), "Initially, no cards should have been drawn.");
    }

    @Test
    void testIncreaseCardsDrawnOnce() {
        state.increaseCardsDrawn(mockGame);
        assertEquals(1, state.getCardsDrawn(), "One card should have been drawn.");
        verify(mockGame, never()).setState(any());
    }

    @Test
    void testIncreaseCardsDrawnTwiceTransitionState() {
        state.increaseCardsDrawn(mockGame);
        state.increaseCardsDrawn(mockGame);
        assertEquals(2, state.getCardsDrawn(), "Two cards should have been drawn.");
        verify(mockGame).setState(any(InfectionState.class));
    }
}
