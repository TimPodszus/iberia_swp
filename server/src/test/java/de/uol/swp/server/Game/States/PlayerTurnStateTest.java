package de.uol.swp.server.Game.States;

import static org.mockito.Mockito.*;

import de.uol.swp.common.game.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTurnStateTest {
    private GameController controller;
    private Action action;
    private Player player;
    private PlayerTurnState state;

    @BeforeEach
    public void setUp() {
        controller = mock(GameController.class);
        action = mock(Action.class);
        player = mock(Player.class);
        state = new PlayerTurnState();
    }

    @Test
    public void testHandleAction_ProcessActionAndCheckTurnOver() {
        when(controller.getCurrentTurn()).thenReturn(mock(GameTurn.class));
        when(controller.getCurrentTurn().isTurnOver()).thenReturn(true);

        state.handleAction(controller, action, player);

        verify(controller.getCurrentTurn()).processAction(action);
        verify(controller).setState(any(DrawCardState.class));
    }
}

