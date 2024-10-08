package de.uol.swp.server.game.States;

import static org.mockito.Mockito.*;

import de.uol.swp.common.game.Action;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.InfectionState;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

 class DrawCardStateTest {
    private GameController controller;
    private Action action;
    private Player player;
    private DrawCardState state;
    private City city;

    @BeforeEach
    public void setUp() {
        controller = mock(GameController.class);
        action = mock(Action.class);
        player = mock(Player.class);
        state = new DrawCardState();
        city = mock(City.class);

        // Setup to return a list of appropriate size
        List<Card> cardList = new ArrayList<>();
        when(player.getCards()).thenReturn(cardList);
    }

    @Test
     void testHandleAction_DrawTwoCardsAndChangeState() {
        when(controller.getCurrentTurn()).thenReturn(mock(GameTurn.class));

        player.getCards().add(new CityCard(1, "Test1", "CityCard", city));
        player.getCards().add(new CityCard(2,"Test2", "CityCard", city));
        player.getCards().add(new CityCard(3,"Test3", "CityCard", city));
        player.getCards().add(new CityCard(4,"Test4", "CityCard", city));
        player.getCards().add(new CityCard(5,"Test5", "CityCard", city));
        player.getCards().add(new CityCard(6,"Test6", "CityCard", city));

        state.handleAction(controller, action, player);
        state.handleAction(controller, action, player);

        verify(controller, times(2)).getCurrentTurn();
        verify(controller.getCurrentTurn(), times(2)).drawPlayerCard();
        verify(controller).setState(any(InfectionState.class));
    }
}


