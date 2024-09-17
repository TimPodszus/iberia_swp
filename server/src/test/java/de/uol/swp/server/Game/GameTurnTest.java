package de.uol.swp.server.Game;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import de.uol.swp.common.user.User;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.plague.PlagueName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.uol.swp.common.enums.Action;
import de.uol.swp.common.enums.ActionType;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.player.Player;

import java.util.ArrayList;
import java.util.List;

public class GameTurnTest {
    private GameTurn gameTurn;
    private Player player;
    private Board board;
    City city1;
    City city2;
    List<City> cityList;
    CityCard cityCard;
    User user;
    @BeforeEach
    public void setUp() {
        player = mock(Player.class);
        board = mock(Board.class);
        gameTurn = new GameTurn(player, board);
    }

    void createTestData(boolean addCityCardToPlayer, boolean addHospitalToCity1) {
        city1 = new City(PlagueName.CHOLERA, "City1", 0, false, addHospitalToCity1);
        city2 = new City(PlagueName.MALARIA, "City2", 0, false, false);
        cityList = new ArrayList<>();
        cityList.add(city1);
        cityList.add(city2);
        user = mock(User.class);
        board = new Board(cityList, 0, 0, null, null, null, null);
        player = new Player(user);
        if (addCityCardToPlayer) {
            cityCard = new CityCard(0, "", "", city1);
            player.addCard(cityCard);
        }
        gameTurn = new GameTurn(player, board);

    }

    @Test
    public void testConstructorInitializesValuesCorrectly() {
        assertEquals(player, gameTurn.getCurrentPlayer());
        assertEquals(board, gameTurn.getBoard());
        assertEquals(4, gameTurn.getActionsRemaining());
        assertFalse(gameTurn.isDrawPhase());
        assertFalse(gameTurn.isInfectionPhase());
        assertFalse(gameTurn.isTurnOver());
    }

    @Test
    public void testProcessActionDecrementsActions() {
        gameTurn.processAction(new Action(ActionType.MOVE));
        assertEquals(3, gameTurn.getActionsRemaining());
    }

    @Test
    public void testProcessActionWithInvalidActionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> gameTurn.processAction(new Action(null)));
    }

    @Test
    public void testCheckTurnEndStartsDrawPhaseIfActionsZero() {
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        assertTrue(gameTurn.isDrawPhase());
        assertFalse(gameTurn.isTurnOver());
    }

    @Test
    public void testEndTurnSetsTurnOver() {
        gameTurn.endTurn();
        assertTrue(gameTurn.isTurnOver());
    }

    @Test
    void testBuildHospital_cityAlreadyHasHospital_throwsException() {
        //given
        createTestData(false, true);

        //when
        Exception exception = assertThrows(Exception.class, () -> {
            gameTurn.buildHospital(city1, false);
        });

        //then
        assertEquals("Die ausgewählte Stadt besitzt bereits ein Krankenhaus!", exception.getMessage());
    }

    @Test
    void testBuildHospital_cityCardRequiredAndPlayerHasCard_buildsHospital() throws Exception {
        //given
        createTestData(true, false);

        //when
        gameTurn.buildHospital(city1, true);

        //then
        assertTrue(city1.isHasHospital());
        assertFalse(player.getCards()
                          .contains(cityCard));
    }

    @Test
    void testBuildHospital_cityCardRequiredButPlayerHasNoCard_throwsException() {
        //given
        createTestData(false, false);

        //when
        Exception exception = assertThrows(Exception.class, () -> {
            gameTurn.buildHospital(city1, true);
        });

        //then
        assertEquals(
                "Der Spieler muss auf der ausgewählten Stadt stehen und die zugehörige Stadtkarte besitzen",
                exception.getMessage()
        );
    }

    @Test
    void testBuildHospital_cityCardNotRequiredButDifferentPlague_throwsException() {
        //given
        createTestData(false, false);

        //when
        Exception exception = assertThrows(Exception.class, () -> {
            gameTurn.buildHospital(city2, false);
        });

        //then
        assertEquals(
                "Der Spieler kann nur auf einer gleichfarbigen Stadt ein Krankenhaus platzieren",
                exception.getMessage()
        );
    }
}
