package de.uol.swp.server.Game;

import de.uol.swp.server.board.Board;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.common.enums.PlagueName;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTurnPlaceHospitalTest {
    private City madrid;
    private City barcelona;
    private GameTurn gameTurn;
    private Player player;
    private CityCard cityCard;

    void createTestData(boolean addCityCardToPlayer) {
        CityRepository cityRepository = new CityRepository();
        Board board = new Board(cityRepository, null, null, 0, 0, null, null, null, null, 0, 0);
        barcelona = board.getCityRepository()
                         .getCitiesByNames(CityName.BARCELONA)
                         .get(0);
        madrid = board.getCityRepository()
                      .getCitiesByNames(CityName.MADRID)
                      .get(0);
        player = new Player(null, barcelona, new ArrayList<>(), null);
        if (addCityCardToPlayer) {
            cityCard = new CityCard(0, "", "", barcelona);
            player.addCard(cityCard);
        }
        gameTurn = new GameTurn(player, board);
    }

    @Test
    void testBuildHospital_cityAlreadyHasHospital_throwsException() {
        createTestData(false);
        barcelona.setHospitalBuilt(true);

        Exception exception = assertThrows(Exception.class, () -> gameTurn.buildHospital(barcelona, false));

        assertEquals("Die ausgewählte Stadt besitzt bereits ein Krankenhaus!", exception.getMessage());
    }

    @Test
    void testBuildHospital_cityCardRequiredAndPlayerHasCard_buildsHospital() throws Exception {
        createTestData(true);

        gameTurn.buildHospital(barcelona, true);

        assertTrue(barcelona.isHospitalBuilt());
        assertFalse(player.getCards()
                          .contains(cityCard));
    }

    @Test
    void testBuildHospital_cityCardRequiredButPlayerHasNoCard_throwsException() {
        createTestData(false);

        Exception exception = assertThrows(Exception.class, () -> gameTurn.buildHospital(barcelona, true));

        assertEquals("Der Spieler muss auf der ausgewählten Stadt stehen und die zugehörige Stadtkarte besitzen",
                exception.getMessage()
        );
    }

    @Test
    void testBuildHospital_cityCardNotRequiredButDifferentPlague_throwsException() {
        createTestData(false);

        Exception exception = assertThrows(Exception.class, () -> gameTurn.buildHospital(madrid, false));

        assertEquals("Der Spieler kann nur auf einer gleichfarbigen Stadt ein Krankenhaus platzieren",
                exception.getMessage()
        );
    }
}