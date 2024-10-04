package de.uol.swp.server.game.states;

import de.uol.swp.common.game.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EventStateTest {
    private GameController controller;
    private Action action;
    private Player player;
    private EventState state;

    @BeforeEach
    public void setUp() {
        controller = mock(GameController.class);
        action = mock(Action.class);
        player = mock(Player.class);
        state = new EventState();
    }

    @Test
    public void testHandleAction_RevertToPreviousState() {
        state.handleAction(controller, action, player);
        verify(controller).setState(controller.getPreviousState());
    }
}

