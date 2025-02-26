package de.uol.swp.server.region.management;

import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.region.RegionRepository;
import de.uol.swp.server.region.data.IRegion;
import de.uol.swp.server.role.*;
import de.uol.swp.server.usermanagement.IUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegionManagementTest {
    @Mock
    private IGame game;
    @InjectMocks
    private RegionManagement regionManagement;
    @Mock
    private PlayerManagement playerManagement;
    @Mock
    private IPlayer requestPlayer;
    @Mock
    private IUser user;
    @Mock
    private IUserDTO userDTO;
    @Mock
    private CityCard cityCard;
    private ICity city;
    private IRegion region1;
    private IRegion region2;
    @Mock
    private RegionRepository regionRepository;
    @Mock
    private PlagueRepository plagueRepository;
    @Mock
    private PlayerTurnState playerTurnState;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        city = mock(ICity.class);
        region1 = mock(IRegion.class);
        region2 = mock(IRegion.class);
        when(game.getRegionRepository()).thenReturn(regionRepository);
    }

    @Test
    void testReduceWaterTreatments_Success() throws RegionManagementException {
        when(city.getName()).thenReturn(CityName.A_CORUNA);
        when(regionRepository.getRegionsByCityName(CityName.A_CORUNA)).thenReturn(List.of(region1, region2));
        when(region1.getWaterTreatments()).thenReturn(3);
        when(region2.getWaterTreatments()).thenReturn(2);

        int remaining = regionManagement.reduceWaterTreatments(game, city, 4);

        assertEquals(0, remaining);
        verify(region1, times(1)).decreaseWaterTreatments(3);
        verify(region2, times(1)).decreaseWaterTreatments(1);
    }

    @Test
    void testReduceWaterTreatments_PartialSuccess() throws RegionManagementException {
        when(city.getName()).thenReturn(CityName.A_CORUNA);
        when(regionRepository.getRegionsByCityName(CityName.A_CORUNA)).thenReturn(List.of(region1, region2));
        when(region1.getWaterTreatments()).thenReturn(1);
        when(region2.getWaterTreatments()).thenReturn(1);

        int remaining = regionManagement.reduceWaterTreatments(game, city, 3);

        assertEquals(1, remaining);
        verify(region1, times(1)).decreaseWaterTreatments(1);
        verify(region2, times(1)).decreaseWaterTreatments(1);
    }

    @Test
    void testReduceWaterTreatments_NoWaterTreatments() throws RegionManagementException {
        when(city.getName()).thenReturn(CityName.A_CORUNA);
        when(regionRepository.getRegionsByCityName(CityName.A_CORUNA)).thenReturn(List.of(region1, region2));
        when(region1.getWaterTreatments()).thenReturn(0);
        when(region2.getWaterTreatments()).thenReturn(0);

        int remaining = regionManagement.reduceWaterTreatments(game, city, 3);

        assertEquals(3, remaining);
        verify(region1, times(1)).decreaseWaterTreatments(0);
        verify(region2, times(1)).decreaseWaterTreatments(0);
    }

    @Test
    void testGetPossibleCityCardsToDiscard_ScientistOfTheRoyalAcademy() throws RegionManagementException {
        IRole role = new ScientistAtTheRoyalAcademy();
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        when(requestPlayer.getRole()).thenReturn(role);
        when(game.getPlayers()).thenReturn(List.of(requestPlayer));
        when(requestPlayer.getUser()).thenReturn(user);
        when(requestPlayer.getUser()
                          .getUsername()).thenReturn("username");
        when(userDTO.getUsername()).thenReturn("username");
        when(requestPlayer.getCards()).thenReturn(List.of(cityCard));
        when(cityCard.getCity()).thenReturn(city);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        when(game.getPlagueRepository()
                 .getPlagues()).thenReturn(List.of());
        when(game.getRegionRepository()
                 .getRegionByID(anyInt())).thenReturn(region1);
        when(region1.getSurroundingCities()).thenReturn(List.of(city));
        when(city.getPlagueName()).thenReturn(PlagueName.MALARIA);

        List<CityCardDTO> result = spyRegionManagement.getPossibleCityCardsToDiscard(userDTO, 1, lobbyId);

        assertEquals(1, result.size());
    }

    @Test
    void testGetPossibleCityCardsToDiscard_NoMatchingCityCards() throws RegionManagementException {
        IRole role = new Sailor();
        ICity city1 = mock(ICity.class);
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        when(requestPlayer.getRole()).thenReturn(role);
        when(game.getPlayers()).thenReturn(List.of(requestPlayer));
        when(requestPlayer.getUser()).thenReturn(user);
        when(requestPlayer.getUser()
                          .getUsername()).thenReturn("username");
        when(userDTO.getUsername()).thenReturn("username");
        when(requestPlayer.getCards()).thenReturn(List.of(cityCard));
        when(cityCard.getCity()).thenReturn(city1);
        when(city1.getPlagueName()).thenReturn(PlagueName.TYPHUS);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        when(game.getPlagueRepository()
                 .getPlagues()).thenReturn(List.of());
        when(game.getRegionRepository()
                 .getRegionByID(anyInt())).thenReturn(region1);
        when(region1.getSurroundingCities()).thenReturn(List.of(city));
        when(city.getPlagueName()).thenReturn(PlagueName.MALARIA);

        List<CityCardDTO> result = spyRegionManagement.getPossibleCityCardsToDiscard(userDTO, 1, lobbyId);

        assertEquals(0, result.size());
    }

    @Test
    void testGetPossibleCityCardsToDiscard_MatchingCityCards() throws RegionManagementException {
        IRole role = new Sailor();
        ICity city1 = mock(ICity.class);
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        when(requestPlayer.getRole()).thenReturn(role);
        when(game.getPlayers()).thenReturn(List.of(requestPlayer));
        when(requestPlayer.getUser()).thenReturn(user);
        when(requestPlayer.getUser()
                          .getUsername()).thenReturn("username");
        when(userDTO.getUsername()).thenReturn("username");
        when(requestPlayer.getCards()).thenReturn(List.of(cityCard));
        when(cityCard.getCity()).thenReturn(city1);
        when(city1.getPlagueName()).thenReturn(PlagueName.MALARIA);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        when(game.getPlagueRepository()
                 .getPlagues()).thenReturn(List.of());
        when(game.getRegionRepository()
                 .getRegionByID(anyInt())).thenReturn(region1);
        when(region1.getSurroundingCities()).thenReturn(List.of(city));
        when(city.getPlagueName()).thenReturn(PlagueName.MALARIA);

        List<CityCardDTO> result = spyRegionManagement.getPossibleCityCardsToDiscard(userDTO, 1, lobbyId);

        assertEquals(1, result.size());
    }

    @Test
    void testGetPossibleCityCardsToDiscard_ResearchedPlagues() throws RegionManagementException {
        IRole role = new Sailor();
        ICity city1 = mock(ICity.class);
        IPlague plague = mock(IPlague.class);
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        when(requestPlayer.getRole()).thenReturn(role);
        when(game.getPlayers()).thenReturn(List.of(requestPlayer));
        when(requestPlayer.getUser()).thenReturn(user);
        when(requestPlayer.getUser()
                          .getUsername()).thenReturn("username");
        when(userDTO.getUsername()).thenReturn("username");
        when(requestPlayer.getCards()).thenReturn(List.of(cityCard));
        when(cityCard.getCity()).thenReturn(city1);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        when(game.getPlagueRepository()
                 .getPlagues()).thenReturn(List.of(plague));
        when(plague.isResearched()).thenReturn(true);
        when(plague.getName()).thenReturn(PlagueName.TYPHUS);
        when(city1.getPlagueName()).thenReturn(PlagueName.TYPHUS);
        when(game.getRegionRepository()
                 .getRegionByID(anyInt())).thenReturn(region1);
        when(region1.getSurroundingCities()).thenReturn(List.of(city));
        when(city.getPlagueName()).thenReturn(PlagueName.MALARIA);

        List<CityCardDTO> result = spyRegionManagement.getPossibleCityCardsToDiscard(userDTO, 1, lobbyId);

        assertEquals(1, result.size());
        assertEquals(
                PlagueName.TYPHUS,
                result.get(0)
                      .getCity()
                      .getPlagueName()
        );
    }

    @Test
    void testGetAvailableRegions_AgriculturalScientist() throws RegionManagementException {
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        IRole role = new AgriculturalScientist();
        when(requestPlayer.getRole()).thenReturn(role);
        when(requestPlayer.getCurrentPosition()).thenReturn(city);
        when(city.getName()).thenReturn(CityName.A_CORUNA);
        when(regionRepository.getRegionsByCityName(CityName.A_CORUNA)).thenReturn(List.of(region1, region2));
        when(game.getPlayers()).thenReturn(List.of(requestPlayer));
        when(requestPlayer.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("username");
        when(userDTO.getUsername()).thenReturn("username");

        Set<IRegionDTO> result = spyRegionManagement.getAvailableRegions(userDTO, lobbyId);

        assertEquals(2, result.size());
        verify(regionRepository, times(1)).getRegionsByCityName(CityName.A_CORUNA);
    }

    @Test
    void testGetAvailableRegions_MatchingCityCardColors() throws RegionManagementException {
        IRole role = new Sailor();
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        when(requestPlayer.getRole()).thenReturn(role);
        when(requestPlayer.getCurrentPosition()).thenReturn(city);
        when(city.getName()).thenReturn(CityName.A_CORUNA);
        when(regionRepository.getRegionsByCityName(CityName.A_CORUNA)).thenReturn(List.of(region1));
        when(game.getPlayers()).thenReturn(List.of(requestPlayer));
        when(requestPlayer.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("username");
        when(userDTO.getUsername()).thenReturn("username");
        when(requestPlayer.getCards()).thenReturn(List.of(cityCard));
        when(cityCard.getCity()).thenReturn(city);
        when(city.getPlagueName()).thenReturn(PlagueName.MALARIA);
        when(region1.getSurroundingCities()).thenReturn(List.of(city));

        Set<IRegionDTO> result = spyRegionManagement.getAvailableRegions(userDTO, lobbyId);

        assertEquals(1, result.size());
        verify(regionRepository, times(1)).getRegionsByCityName(CityName.A_CORUNA);
    }

    @Test
    void testGetAvailableRegions_NoMatchingCityCardColors() throws RegionManagementException {
        IRole role = new Sailor();
        ICity city1 = mock(ICity.class);
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        when(requestPlayer.getRole()).thenReturn(role);
        when(requestPlayer.getCurrentPosition()).thenReturn(city);
        when(city.getName()).thenReturn(CityName.A_CORUNA);
        when(regionRepository.getRegionsByCityName(CityName.A_CORUNA)).thenReturn(List.of(region1));
        when(game.getPlayers()).thenReturn(List.of(requestPlayer));
        when(requestPlayer.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("username");
        when(userDTO.getUsername()).thenReturn("username");
        when(requestPlayer.getCards()).thenReturn(List.of(cityCard));
        when(cityCard.getCity()).thenReturn(city1);
        when(city.getPlagueName()).thenReturn(PlagueName.TYPHUS);
        when(city1.getPlagueName()).thenReturn(PlagueName.MALARIA);
        when(region1.getSurroundingCities()).thenReturn(List.of(city));

        Set<IRegionDTO> result = spyRegionManagement.getAvailableRegions(userDTO, lobbyId);

        assertEquals(0, result.size());
        verify(regionRepository, times(1)).getRegionsByCityName(CityName.A_CORUNA);
    }

    @Test
    void testIncreaseWaterTreatmentsFromRegion_Success() throws RegionManagementException, GameManagementException, PlayerManagementException {
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        when(game.getState()).thenReturn(playerTurnState);
        when(game.getRegionRepository()
                 .getRegionByID(anyInt())).thenReturn(region1);
        when(game.getCurrentPlayer()).thenReturn(requestPlayer);
        when(requestPlayer.getUser()).thenReturn(user);

        spyRegionManagement.increaseWaterTreatmentsFromRegion(lobbyId, 1, 5, cityCard, user);

        verify(region1, times(1)).increaseWaterTreatments(5);
        verify(playerManagement, times(1)).discardPlayerCard(
                lobbyId,
                requestPlayer.getUser()
                             .getUsername(),
                cityCard.getId()
        );
        verify(playerTurnState, times(1)).reduceActionsRemaining(game);
    }

    @Test
    void testIncreaseWaterTreatmentsFromRegion_PlayerNotCurrent() throws PlayerManagementException {
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        when(game.getState()).thenReturn(playerTurnState);
        when(game.getRegionRepository()
                 .getRegionByID(anyInt())).thenReturn(region1);
        when(game.getCurrentPlayer()).thenReturn(requestPlayer);
        when(user.getUsername()).thenReturn("username");
        IUser differentUser = mock(IUser.class);
        when(differentUser.getUsername()).thenReturn("differentUsername");
        when(requestPlayer.getUser()).thenReturn(differentUser);

        assertThrows(
                GameManagementException.class,
                () -> spyRegionManagement.increaseWaterTreatmentsFromRegion(lobbyId, 1, 5, cityCard, user)
        );

        verify(region1, never()).increaseWaterTreatments(anyInt());
        verify(playerManagement, never()).discardPlayerCard(anyString(), any(), any());
        verify(playerTurnState, never()).reduceActionsRemaining(any());
    }

    @Test
    void testIncreaseWaterTreatmentsFromRegion_NotPlayerTurnState() throws RegionManagementException, PlayerManagementException {
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        DrawCardState drawCardState = mock(DrawCardState.class);
        when(game.getState()).thenReturn(drawCardState);

        assertThrows(
                GameManagementException.class,
                () -> spyRegionManagement.increaseWaterTreatmentsFromRegion(lobbyId, 1, 5, cityCard, user)
        );

        verify(region1, never()).increaseWaterTreatments(anyInt());
        verify(playerManagement, never()).discardPlayerCard(anyString(), any(), any());
        verify(playerTurnState, never()).reduceActionsRemaining(any());
    }

    @Test
    void testGetPossibleCityCardsToDiscard_PlayerNotFound() throws RegionManagementException {
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        when(game.getPlayers()).thenReturn(List.of());

        RegionManagementException exception = assertThrows(
                RegionManagementException.class,
                () -> spyRegionManagement.getPossibleCityCardsToDiscard(userDTO, 1, lobbyId)
        );

        assertEquals("Request player not found", exception.getMessage());
    }

    @Test
    void testGetAvailableRegions_PlayerNotFound() throws RegionManagementException {
        String lobbyId = "testLobbyId";
        RegionManagement spyRegionManagement = spy(regionManagement);
        doReturn(game).when(spyRegionManagement)
                      .getGame(lobbyId);
        when(game.getPlayers()).thenReturn(List.of());

        RegionManagementException exception = assertThrows(
                RegionManagementException.class,
                () -> spyRegionManagement.getAvailableRegions(userDTO, lobbyId)
        );

        assertEquals("Request player not found", exception.getMessage());
    }
}