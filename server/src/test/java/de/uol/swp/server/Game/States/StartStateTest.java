package de.uol.swp.server.Game.States;

import static org.mockito.Mockito.*;

import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.states.StartState;
import de.uol.swp.server.game.states.WaitForPositioning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

