package de.uol.swp.server.game.states;
/*
import static org.mockito.Mockito.*;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.game.states.InfectionState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
*/
public class InfectionStateTest {
    /*
    !!!!!!!!!!!!!
    Wartet auf die Implementierung der Methode InfectCity
    !!!!!!!!!!!!!
    private GameController controller;
    private Action action;
    private Player player;
    private InfectionState state;
    private GameTurn turn;
    private Board board;
    private List<Card> infectionCards;
    private InfectionCard infectionCard;

    @BeforeEach
    public void setUp() {
        controller = mock(GameController.class);
        action = mock(Action.class);
        player = mock(Player.class);
        state = mock(InfectionState.class);
        turn = mock(GameTurn.class);
        board = mock(Board.class);
        infectionCard = mock(InfectionCard.class);
        infectionCards = new ArrayList<>();

        when(controller.getCurrentTurn()).thenReturn(turn);
        when(controller.getBoard()).thenReturn(board);
        when(turn.drawInfectionCard()).thenReturn(infectionCard);
        when(board.getInfectionCounter()).thenReturn(2);

        infectionCards.add(infectionCard);
        infectionCards.add(infectionCard);
        doReturn(infectionCards).when(board).getInfectionCardDrawPile();
    }

    @Test
    public void testHandleAction_InfectCities() {
        state.handleAction(controller, action, player);
        state.handleAction(controller, action, player);

        verify(turn, times(2)).infectCity(infectionCard, 1);
        verify(controller).setCurrentTurn(any(GameTurn.class));
        verify(controller).setState(any(PlayerTurnState.class));
    }
    */
}

