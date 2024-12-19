package de.uol.swp.server.game.management;

import de.uol.swp.common.user.User;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.management.CityManagement;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.Player;
import de.uol.swp.server.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameManagementTest {

    @Mock
    private IGame game;
    @InjectMocks
    private GameManagement gameManagement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePlayers() {
        List<User> users = Arrays.asList(mock(User.class), mock(User.class));
        List<Player> players = new ArrayList<>();
        when(game.getPlayers()).thenReturn(players);

        gameManagement.createPlayers(users, game);

        assertEquals(2, players.size(), "Es sollten zwei Spieler erstellt werden");
    }

    @Test
    void testSetStartingPlayer() {
        Player firstPlayer = mock(Player.class);
        Player secondPlayer = mock(Player.class);

        CityCard firstCityCard = mock(CityCard.class);
        CityCard secondCityCard = mock(CityCard.class);

        City city1 = mock(City.class);
        City city2 = mock(City.class);

        when(firstCityCard.getCity()).thenReturn(city1);
        when(secondCityCard.getCity()).thenReturn(city2);

        when(city1.getFoundationDate()).thenReturn(1700);
        when(city2.getFoundationDate()).thenReturn(1600);

        when(firstPlayer.getCards()).thenReturn(Collections.singletonList(firstCityCard));
        when(secondPlayer.getCards()).thenReturn(Collections.singletonList(secondCityCard));

        List<Player> players = new ArrayList<>(Arrays.asList(firstPlayer, secondPlayer));
        when(game.getPlayers()).thenReturn(players);

        gameManagement.setStartingPlayer(game);

        assertEquals(secondPlayer, players.get(0), "Der Spieler mit der ältesten Stadt sollte der Startspieler sein");
        assertEquals(firstPlayer, players.get(1), "Der andere Spieler sollte an die zweite Position verschoben werden");
    }


    @Test
    void testAssignRoles() {
        when(game.getPlayers()).thenReturn(Arrays.asList(mock(Player.class), mock(Player.class)));
        gameManagement.assignRoles(game);

        verify(game.getPlayers()
                .get(0)).setRole(any(Role.class));
        verify(game.getPlayers()
                .get(1)).setRole(any(Role.class));
    }

    @Test
    void testInitiateInfections() {
        CityManagement cityManagement = mock(CityManagement.class);
        when(game.getCityManagement()).thenReturn(cityManagement);

        gameManagement.initiateInfections(game);

        verify(cityManagement, times(9)).infectCity(any(), anyInt());
    }

    @Test
    void testGameManagementException() {
        GameManagementException exception = assertThrows(GameManagementException.class, () -> {
            throw new GameManagementException("Test Exception");
        });

        assertEquals("Test Exception", exception.getMessage());
    }
}
