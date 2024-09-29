package de.uol.swp.server.Game.States;

import static org.mockito.Mockito.*;

import de.uol.swp.common.game.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

