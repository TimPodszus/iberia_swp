package de.uol.swp.server.Game.States;

import static org.mockito.Mockito.*;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.game.Action;
import de.uol.swp.common.game.MoveAction;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForPositioning;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class WaitForPositioningTest {
    private GameController controller;
    private Player player;
    private WaitForPositioning state;
    private MoveAction action;
    private PlayerTurnState playerTurnState;
    @BeforeEach
    public void setUp() {
        controller = mock(GameController.class);
        player = mock(Player.class);
        state = new WaitForPositioning();
        action = mock(MoveAction.class);
        playerTurnState = mock(PlayerTurnState.class);
        when(action.getDestination()).thenReturn(mock(CityDTO.class));
        when(action.getDestination().getName()).thenReturn("Barcelona");
        List<Player> mockedPlayers = new ArrayList<>();
        mockedPlayers.add(player);
        when(controller.getPlayers()).thenReturn(mockedPlayers);
    }

    @Test
    public void testHandleAction_SetsPlayerStartingPositionAndChangesState() throws Exception {
        state.handleAction(controller, action, player);

        verify(player).setStartingPosition("Barcelona");
        verify(controller).setState(any(PlayerTurnState.class));
    }

    @Test
    public void testHandleAction_WithInvalidActionDoesNotSetPositionOrChangeState() throws Exception {
        Action invalidAction = mock(Action.class); // Not a MoveAction
        state.handleAction(controller, invalidAction, player);

        verify(player, never()).setStartingPosition(anyString());
        verify(controller, never()).setState(any());
    }
}

