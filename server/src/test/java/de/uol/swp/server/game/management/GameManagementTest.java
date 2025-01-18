package de.uol.swp.server.game.management;

import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForPositioning;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.IUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static de.uol.swp.common.city.CityName.ALBACETE;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link GameManagement} class.
 * This class ensures the correct functionality of game management methods, such as creating players,
 * setting the starting player, assigning roles, and initiating infections.
 */
class GameManagementTest {

    @Mock
    private GameStore gameStore;

    @Mock
    private CreateGameRequest createGameRequest;

    @Mock
    private PositioningRequest positioningRequest;

    @InjectMocks
    private GameManagement gameManagement;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        setGameStoreInstance(gameStore);

        UserDTO userDTO1 = new UserDTO("test", "test");
        UserDTO userDTO2 = new UserDTO("test2", "test2");

        when(createGameRequest.getDifficulty()).thenReturn(1);
        when(createGameRequest.getLobbyId()).thenReturn("lobby123");
        when(createGameRequest.getUsers()).thenReturn(List.of(userDTO1, userDTO2));
    }

    private void setGameStoreInstance(GameStore mock) throws Exception {
        Field instance = GameStore.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, mock);
    }

    @Test
    void testCreateAndInitializeGame() throws Exception, PlayerManagementException {
        ArgumentCaptor<IGame> gameCaptor = ArgumentCaptor.forClass(IGame.class);
        gameManagement.createAndInitializeGame(createGameRequest);
        verify(gameStore).addGame(eq("lobby123"), gameCaptor.capture());
        IGame capturedGame = gameCaptor.getValue();
        assertNotNull(capturedGame);
        assertEquals(1, capturedGame.getDifficulty());
        assertEquals("lobby123", capturedGame.getGameId());
    }

    @Test
    void testSetPositioning_InvalidLobbyCode() {
        when(gameStore.getGame("invalidLobby")).thenReturn(null);
        when(positioningRequest.getLobbyId()).thenReturn("invalidLobby");

        assertThrows(NullPointerException.class, () -> gameManagement.setPositioning(positioningRequest));
    }
    @Test
    void testSetPositioning_InvalidGameState() throws GameManagementException {
        IGame mockGame = mock(IGame.class);
        when(gameStore.getGame("lobby123")).thenReturn(mockGame);
        when(mockGame.getState()).thenReturn(mock(PlayerTurnState.class));
        when(positioningRequest.getLobbyId()).thenReturn("lobby123");

        gameManagement.setPositioning(positioningRequest);
        verify(mockGame, never()).getPlayers();
    }

    @Test
    void testSetPositioning_PlayerNotFound() {
        IGame mockGame = mock(IGame.class);
        WaitForPositioning mockState = mock(WaitForPositioning.class);
        Session mockSession = mock(Session.class);
        IUserDTO mockUser = mock(IUserDTO.class);

        when(gameStore.getGame("lobby123")).thenReturn(mockGame);
        when(mockGame.getState()).thenReturn(mockState);
        when(mockGame.getPlayers()).thenReturn(List.of());
        when(positioningRequest.getLobbyId()).thenReturn("lobby123");
        when(mockSession.getUser()).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("testUsername");
        when(positioningRequest.getSession()).thenReturn(Optional.of(mockSession));

        assertThrows(AssertionError.class, () -> gameManagement.setPositioning(positioningRequest));
    }

    @Test
    void testSetPositioning_AllPlayersPositioned() throws Exception {
        IGame mockGame = mock(IGame.class);
        WaitForPositioning mockState = mock(WaitForPositioning.class);
        Player mockPlayer = spy(new Player(mock(IUser.class)));
        IUser mockUser = mock(IUser.class);
        IUserDTO mockUserDTO = mock(IUserDTO.class);
        Session mockSession = mock(Session.class);
        CityRepository mockCityRepository = mock(CityRepository.class);

        when(mockUser.getUsername()).thenReturn("testUsername");
        when(mockUserDTO.getUsername()).thenReturn("testUsername");

        CityCard mockCityCard = mock(CityCard.class);
        ICity mockCity = mock(ICity.class);
        when(mockCity.getName()).thenReturn(ALBACETE);
        when(mockCityCard.getCity()).thenReturn(mockCity);

        List<Card> cards = new ArrayList<>();
        cards.add(mockCityCard);
        when(mockPlayer.getCards()).thenReturn(cards);

        List<Player> playerList = new ArrayList<>();
        playerList.add(mockPlayer);

        when(gameStore.getGame("lobby123")).thenReturn(mockGame);
        when(mockGame.getState()).thenReturn(mockState);
        when(mockGame.getPlayers()).thenReturn(playerList);
        when(mockGame.getCityRepository()).thenReturn(mockCityRepository);
        when(mockCityRepository.getCitiesByNames(any(CityName.class)))
                .thenReturn(List.of(mockCity));

        when(mockPlayer.getUser()).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("testUsername");

        when(positioningRequest.getLobbyId()).thenReturn("lobby123");
        when(positioningRequest.getCityId()).thenReturn(34);
        when(mockSession.getUser()).thenReturn(mockUserDTO);
        when(positioningRequest.getSession()).thenReturn(Optional.of(mockSession));

        when(mockCityRepository.getCityNameById(34)).thenReturn(ALBACETE);

        when(mockState.getPositionedPlayersCount()).thenReturn(1);

        gameManagement.setPositioning(positioningRequest);

        verify(mockGame).setState(any(PlayerTurnState.class));
        verify(mockGame).setCurrentPlayerIndex(0);
    }

    @Test
    void testGameManagementException() {
        GameManagementException exception = assertThrows(GameManagementException.class, () -> {
            throw new GameManagementException("Test Exception");
        });

        assertEquals("Test Exception", exception.getMessage());
    }

    @Test
    void testGetAvailableActions() {
        List<GameActions> actions = gameManagement.getAvailableActions("LobbyId", null);
        assertEquals(6, actions.size());
    }
}
