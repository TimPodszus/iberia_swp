package de.uol.swp.server.Game;

import de.uol.swp.common.user.User;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.plague.PlagueName;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameTurnPlaceHospitalTest
{
    private GameTurn gameTurn;
    private Player player;
    City city1;
    City city2;
    List<City> cityList;
    CityCard cityCard;
    User user;

    void createTestData(boolean addCityCardToPlayer, boolean addHospitalToCity1)
    {
        city1 = new City(PlagueName.CHOLERA, "City1", 0, false, addHospitalToCity1);
        city2 = new City(PlagueName.MALARIA, "City2", 0, false, false);
        cityList = new ArrayList<>();
        cityList.add(city1);
        cityList.add(city2);
        user = mock(User.class);
        Board board = new Board(cityList, 0, 0, null, null, null, null, 0, 0);
        player = new Player(null, city1, new ArrayList<>(), user);
        if (addCityCardToPlayer) {
            cityCard = new CityCard(0, "", "", city1);
            player.addCard(cityCard);
        }
        gameTurn = new GameTurn(player, board);
    }

    @Test
    void testBuildHospital_cityAlreadyHasHospital_throwsException()
    {
        //given
        createTestData(false, true);

        //when
        Exception exception = assertThrows(Exception.class, () -> gameTurn.buildHospital(city1, false));

        //then
        assertEquals("Die ausgewählte Stadt besitzt bereits ein Krankenhaus!", exception.getMessage());
    }

    @Test
    void testBuildHospital_cityCardRequiredAndPlayerHasCard_buildsHospital() throws Exception
    {
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
    void testBuildHospital_cityCardRequiredButPlayerHasNoCard_throwsException()
    {
        //given
        createTestData(false, false);

        //when
        Exception exception = assertThrows(Exception.class, () -> gameTurn.buildHospital(city1, true));

        //then
        assertEquals(
                "Der Spieler muss auf der ausgewählten Stadt stehen und die zugehörige Stadtkarte besitzen",
                exception.getMessage()
        );
    }

    @Test
    void testBuildHospital_cityCardNotRequiredButDifferentPlague_throwsException()
    {
        //given
        createTestData(false, false);

        //when
        Exception exception = assertThrows(Exception.class, () -> gameTurn.buildHospital(city2, false));

        //then
        assertEquals(
                "Der Spieler kann nur auf einer gleichfarbigen Stadt ein Krankenhaus platzieren",
                exception.getMessage()
        );
    }
}
