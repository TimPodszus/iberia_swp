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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

/**
 * Test class for the {@link GameManagement} class.
 * This class ensures the correct functionality of game management methods, such as creating players,
 * setting the starting player, assigning roles, and initiating infections.
 */
class GameManagementTest {

    /**
     * Mock object for {@link IGame}, representing the game being managed.
     */
    @Mock
    private IGame game;

    /**
     * Injected instance of {@link GameManagement}, under test.
     */
    @InjectMocks
    private GameManagement gameManagement;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@link GameManagement#createPlayers(List, IGame)} method.
     * Verifies that the correct number of players is created based on the provided list of users.
     */
    @Test
    void testCreatePlayers() {
        List<User> users = Arrays.asList(mock(User.class), mock(User.class));
        List<Player> players = new ArrayList<>();
        when(game.getPlayers()).thenReturn(players);

        gameManagement.createPlayers(users, game);

        assertEquals(2, players.size(), "Es sollten zwei Spieler erstellt werden");
    }

    /**
     * Tests the {@link GameManagement#setStartingPlayer(IGame)} method.
     * Ensures the player with the oldest city's foundation date is set as the starting player.
     */
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

    /**
     * Tests the {@link GameManagement#assignRoles(IGame)} method.
     * Ensures that roles are assigned to all players in the game.
     */
    @Test
    void testAssignRoles() {
        when(game.getPlayers()).thenReturn(Arrays.asList(mock(Player.class), mock(Player.class)));
        gameManagement.assignRoles(game);

        verify(game.getPlayers()
                .get(0)).setRole(any(Role.class));
        verify(game.getPlayers()
                .get(1)).setRole(any(Role.class));
    }

    /**
     * Tests the {@link GameManagement#initiateInfections(IGame)} method.
     * Verifies that cities are infected correctly during game initialization.
     */
    @Test
    void testInitiateInfections() {
        CityManagement cityManagement = mock(CityManagement.class);
        when(game.getCityManagement()).thenReturn(cityManagement);

        gameManagement.initiateInfections(game);

        verify(cityManagement, times(9)).infectCity(any(), anyInt());
    }

    /**
     * Tests the custom {@link GameManagementException}.
     * Ensures that the exception can be thrown and contains the correct message.
     */
    @Test
    void testGameManagementException() {
        GameManagementException exception = assertThrows(GameManagementException.class, () -> {
            throw new GameManagementException("Test Exception");
        });

        assertEquals("Test Exception", exception.getMessage());
    }
}
