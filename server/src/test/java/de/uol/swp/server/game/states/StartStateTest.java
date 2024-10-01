package de.uol.swp.server.game.states;

import de.uol.swp.server.game.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StartStateTest {
    private GameController controller;
    private StartState state;

    @BeforeEach
    public void setUp() {
        controller = mock(GameController.class);
        state = new StartState();
    }

    @Test
    public void testHandleAction_InitializesGameAndSetsNextState() {
        state.handleAction(controller, null, null);

        verify(controller).initializeGame();
        verify(controller).setState(any(WaitForPositioning.class));
    }
}

