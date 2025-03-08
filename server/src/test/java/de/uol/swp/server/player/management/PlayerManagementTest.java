package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.data.ICardDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.EpidemicCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.role.Sailor;
import de.uol.swp.server.role.ScientistAtTheRoyalAcademy;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for PlayerManagement.
 */
class PlayerManagementTest {

    private final IGame game = new Game(1, "123");

    @Mock
    private Player player;
    @Mock
    private IUser user;
    @Mock
    private EpidemicCard epidemicCard;
    @Mock
    private IGameManagement gameManagement;
    @Mock
    private ICityManagement cityManagement;

    private IPlayerManagement playerManagement;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        GameStore.getInstance()
                 .addGame(game.getGameId(), game);
        playerManagement = new PlayerManagement(cityManagement);
    }

    /**
     * Tests that drawPlayerCard throws an exception when the player is not found.
     */
    @Test
    void drawPlayerCard_PlayerNotFound_ThrowsException() {
        assertThrows(PlayerManagementException.class, () -> playerManagement.drawPlayerCard(game.getGameId(), user));
    }

    /**
     * Tests that drawPlayerCard draws a card for a valid player.
     *
     * @throws PlayerManagementException if an error occurs while drawing the card
     */
    @Test
    void drawPlayerCard_ValidPlayer_DrawsCard() throws PlayerManagementException {
        game.getPlayers()
            .add(player);
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");

        ICardDTO cardDTO = playerManagement.drawPlayerCard(game.getGameId(), user);

        assertNotNull(cardDTO);
    }

    /**
     * Tests that drawPlayerCard increases the infection counter when an EpidemicCard is drawn.
     *
     * @throws PlayerManagementException if an error occurs while drawing the card
     */
    @Test
    void drawPlayerCard_EpidemicCard_IncreasesInfectionCounter() throws PlayerManagementException {
        game.getPlayers()
            .add(player);
        game.getPlayerCardDrawPile()
            .removeIf(currentCard -> !(currentCard instanceof EpidemicCard));
        game.getPlayerCardDrawPile()
            .add(epidemicCard);
        game.setState(new DrawCardState());
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");
        when(gameManagement.drawInfectionCard(game)).thenReturn(new InfectionCard(1, "test", mock(ICity.class)));

        playerManagement.drawPlayerCard(game.getGameId(), user);

        assertEquals(2, game.getInfectionCounter());
    }

    /**
     * Tests that setStartingPosition throws an exception when an invalid city is provided.
     */
    @Test
    void setStartingPosition_InvalidCity_ThrowsException() {
        CityName requestedCity = CityName.ALBACETE;
        CityName heldCity = CityName.ZARAGOZA;

        ICity heldCityObject = mock(ICity.class);
        when(heldCityObject.getName()).thenReturn(heldCity);

        CityCard heldCityCard = new CityCard(1, "test", heldCityObject);
        when(player.getCards()).thenReturn(List.of(heldCityCard));

        PlayerManagementException thrown = assertThrows(
                PlayerManagementException.class,
                () -> playerManagement.setStartingPosition(game.getGameId(), requestedCity, player),
                "Expected setStartingPosition() to throw, but it did not"
        );

        assertTrue(thrown.getMessage()
                         .contains("Keine valide Stadt ausgewählt"));
    }

    /**
     * Tests that addCard adds a card to the player's hand.
     */
    @Test
    void addCard_AddsCardToPlayer() {
        ICard card = new CityCard(1, "test", mock(ICity.class));
        List<ICard> cards = new ArrayList<>(List.of(card));
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");
        game.getPlayers()
            .add(player);
        when(player.getCards()).thenReturn(cards);

        playerManagement.addCard(
                game.getGameId(),
                player.getUser()
                      .getUsername(),
                card
        );

        assertTrue(cards.contains(card));
    }

    /**
     * Tests that discardCard discards a single card from the player's hand.
     */
    @Test
    void discardCard_DiscardSingleCard() throws GameException {
        ICard infectionCard = new InfectionCard(1, "test", mock(ICity.class));
        ArrayList<ICard> cards = new ArrayList<>();
        cards.add(infectionCard);
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");
        when(player.getCards()).thenReturn(cards);
        game.getPlayers()
            .add(player);

        playerManagement.discardPlayerCard(
                game.getGameId(),
                player.getUser()
                      .getUsername(),
                infectionCard.getId()
        );

        assertTrue(game.getPlayerCardDiscardPile()
                       .contains(infectionCard));
    }

    /**
     * Tests that discardCards discards multiple cards from the player's hand.
     */
    @Test
    void discardCards_DiscardMultipleCards() throws GameException {
        ICard card1 = new InfectionCard(1, "test", mock(ICity.class));
        ICard card2 = new CityCard(1, "test", mock(ICity.class));
        ArrayList<ICard> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);

        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");
        when(player.getCards()).thenReturn(cards);
        game.getPlayers()
            .add(player);

        playerManagement.discardPlayerCards(
                game.getGameId(),
                player.getUser()
                      .getUsername(),
                cards.stream()
                     .map(ICard::getId)
                     .toList()
        );

        assertTrue(game.getPlayerCardDiscardPile()
                       .contains(card1));
        assertTrue(game.getPlayerCardDiscardPile()
                       .contains(card2));
    }

    /**
     * Tests that setPlayerLocation sets the player's location to the specified city.
     *
     * @throws PlayerManagementException if an error occurs while setting the player's location
     */
    @Test
    void testSetPlayerLocation() throws PlayerManagementException {
        ICity city = new City(1, PlagueName.CHOLERA, CityName.ALBACETE, 1234, false);
        when(cityManagement.getCity(game.getGameId(), 1)).thenReturn(city);

        IUser testUser = new User("testUser", "testPassword");
        IPlayer testPlayer = new Player(testUser, game.getGameId());
        game.getPlayers()
            .add(testPlayer);
        GameStore.getInstance()
                 .addGame(game.getGameId(), game);

        playerManagement.setPlayerLocation(game.getGameId(), "testUser", 1);
        assertEquals(city, testPlayer.getCurrentPosition());
    }

    @Test
    void testShuffleInfectionCardsFromDrawPile() {
        CityRepository cityRepository = mock(CityRepository.class);
        ICity barcelona = mock(ICity.class);
        ICity alicante = mock(ICity.class);
        ICity madrid = mock(ICity.class);

        when(cityRepository.getCityByName(CityName.BARCELONA)).thenReturn(barcelona);
        when(cityRepository.getCityByName(CityName.ALICANTE)).thenReturn(alicante);
        when(cityRepository.getCityByName(CityName.MADRID)).thenReturn(madrid);

        InfectionCard infectionCard1 = new InfectionCard(1, "InfectionCard1", barcelona);
        InfectionCard infectionCard2 = new InfectionCard(2, "InfectionCard2", alicante);
        InfectionCard infectionCard3 = new InfectionCard(3, "InfectionCard3", madrid);

        List<InfectionCard> discardPile = new ArrayList<>(List.of(infectionCard1, infectionCard2));
        List<InfectionCard> drawPile = new ArrayList<>(List.of(infectionCard3));

        IGame mockGame = mock(Game.class);
        when(mockGame.getInfectionCardDiscardPile()).thenReturn(discardPile);
        when(mockGame.getInfectionCardDrawPile()).thenReturn(drawPile);

        playerManagement.shuffleInfectionCardsFromDrawPile(mockGame);

        assertTrue(drawPile.contains(infectionCard1), "Expected draw pile to contain infectionCard1");
        assertTrue(drawPile.contains(infectionCard2), "Expected draw pile to contain infectionCard2");
        assertTrue(drawPile.contains(infectionCard3), "Expected draw pile to contain infectionCard3");
        assertTrue(discardPile.isEmpty(), "Expected discard pile to be empty after shuffling");
    }


    @Test
    void testDrawBottomInfectionCard() {
        CityRepository cityRepository = new CityRepository();
        InfectionCard infectionCard1 = new InfectionCard(
                1,
                "InfectionCard1",
                cityRepository.getCityByName(CityName.BARCELONA)
        );
        InfectionCard infectionCard2 = new InfectionCard(
                2,
                "InfectionCard2",
                cityRepository.getCityByName(CityName.ALICANTE)
        );
        game.getInfectionCardDrawPile()
            .add(infectionCard1);
        game.getInfectionCardDrawPile()
            .add(infectionCard2);

        InfectionCard drawnCard = playerManagement.drawBottomInfectionCard(game);

        assertEquals(
                infectionCard2,
                drawnCard,
                "Expected the last infection card to be drawn from the bottom of the draw pile"
        );
    }

    @Test
    void testDrawBottomInfectionCardWithEmptyDiscardPile() {
        game.getInfectionCardDrawPile()
            .clear();

        assertThrows(
                IllegalStateException.class,
                () -> playerManagement.drawBottomInfectionCard(game),
                "Expected " + "IllegalStateException when the discard pile is empty"
        );
    }

    @Test
    void testGetCardsToSort() throws GameException, IllegalGameStateException {
        ICard card = mock(ICard.class);
        game.setState(new PlayerTurnState());
        game.getPlayerCardDrawPile()
            .add(card);
        game.getPlayerCardDrawPile()
            .add(card);
        game.getPlayerCardDrawPile()
            .add(card);
        game.getPlayers()
            .clear();
        game.getPlayers()
            .add(player);
        when(player.getUser()).thenReturn(user);
        IPlayerManagement spyPlayerManagement = spy(playerManagement);
        doReturn(game).when(spyPlayerManagement)
                      .getGame("lobbyId");
        when(player.getRole()).thenReturn(new ScientistAtTheRoyalAcademy());

        List<ICardDTO> cards = spyPlayerManagement.getCardsToSort("lobbyId", user);
        assertNotNull(cards);
    }

    @Test
    void testSortCards() throws GameException {
        ICard card1 = mock(ICard.class);
        ICard card2 = mock(ICard.class);
        ICard card3 = mock(ICard.class);
        when(card1.getId()).thenReturn(1);
        when(card2.getId()).thenReturn(2);
        when(card3.getId()).thenReturn(3);

        game.setState(new PlayerTurnState());
        game.getPlayerCardDrawPile()
            .clear();
        game.getPlayerCardDrawPile()
            .add(card1);
        game.getPlayerCardDrawPile()
            .add(card2);
        game.getPlayerCardDrawPile()
            .add(card3);
        game.getPlayers()
            .clear();
        game.getPlayers()
            .add(player);
        when(player.getUser()).thenReturn(user);
        IPlayerManagement spyPlayerManagement = spy(playerManagement);
        doReturn(game).when(spyPlayerManagement)
                      .getGame("lobbyId");
        when(player.getRole()).thenReturn(new ScientistAtTheRoyalAcademy());

        List<ICardDTO> cards = new ArrayList<>();
        ICardDTO cardDTO1 = mock(ICardDTO.class);
        ICardDTO cardDTO2 = mock(ICardDTO.class);
        ICardDTO cardDTO3 = mock(ICardDTO.class);
        when(cardDTO1.getId()).thenReturn(3);
        when(cardDTO2.getId()).thenReturn(2);
        when(cardDTO3.getId()).thenReturn(1);
        cards.add(cardDTO1);
        cards.add(cardDTO2);
        cards.add(cardDTO3);

        spyPlayerManagement.sortCards("lobbyId", user, cards);

        List<ICard> sortedDrawPile = game.getPlayerCardDrawPile();
        assertEquals(card3, sortedDrawPile.get(0));
        assertEquals(card2, sortedDrawPile.get(1));
        assertEquals(card1, sortedDrawPile.get(2));
    }

    @Test
    void testDrawPlayerCard_EmptyDrawPile_ThrowsException() {
        game.getPlayerCardDrawPile()
            .clear();
        game.getPlayers()
            .add(player);
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");

        assertThrows(PlayerManagementException.class, () -> playerManagement.drawPlayerCard(game.getGameId(), user));
    }

    @Test
    void testSetPlayerLocation_PlayerNotFound_ThrowsException() {
        game.getPlayers()
            .clear();

        assertThrows(
                PlayerManagementException.class,
                () -> playerManagement.setPlayerLocation(game.getGameId(), "nonExistentPlayer", 1)
        );
    }

    @Test
    void testGetCard_PlayerHasCard_ReturnsCard() throws PlayerManagementException {
        ICard card = mock(ICard.class);
        when(card.getId()).thenReturn(1);
        game.getPlayers()
            .add(player);
        when(player.getCards()).thenReturn(List.of(card));
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");

        ICard result = playerManagement.getCard(game.getGameId(), "testUser", 1);

        assertEquals(card, result);
    }

    @Test
    void testSortCards_NotCurrentPlayerOrNotScientist_ThrowsException() {
        ICardDTO cardDTO = mock(ICardDTO.class);
        List<ICardDTO> cards = List.of(cardDTO);
        game.getPlayers()
            .add(player);
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");
        when(player.getRole()).thenReturn(new Sailor());

        IPlayerManagement spyPlayerManagement = spy(playerManagement);
        doReturn(game).when(spyPlayerManagement)
                      .getGame("lobbyId");
        doReturn(player).when(spyPlayerManagement)
                        .getPlayerByUser(user, game);

        GameException thrown = assertThrows(
                GameException.class,
                () -> spyPlayerManagement.sortCards("lobbyId", user, cards),
                "Expected sortCards() to throw, but it did not"
        );

        assertTrue(thrown.getMessage()
                         .contains("It is not your turn or your role is not scientist of the royal academy"));
    }

    @Test
    void testGetCardsToSort_NotCurrentPlayerOrNotScientist_ThrowsException() {
        game.getPlayers()
            .add(player);
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");
        game.setState(new PlayerTurnState());
        when(player.getRole()).thenReturn(new Sailor());

        IPlayerManagement spyPlayerManagement = spy(playerManagement);
        doReturn(game).when(spyPlayerManagement)
                      .getGame("lobbyId");
        doReturn(player).when(spyPlayerManagement)
                        .getPlayerByUser(user, game);

        GameException thrown = assertThrows(
                GameException.class,
                () -> spyPlayerManagement.getCardsToSort("lobbyId", user),
                "Expected getCardsToSort() to throw, but it did not"
        );

        assertTrue(thrown.getMessage()
                         .contains("It is not your turn or your role is not scientist of the royal academy"));
    }

    @Test
    void testGetCardsToSort_NotPlayerTurnState_ThrowsException() {
        game.setState(new DrawCardState()); // Not PlayerTurnState
        game.getPlayers()
            .add(player);
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");
        when(player.getRole()).thenReturn(new ScientistAtTheRoyalAcademy());

        IPlayerManagement spyPlayerManagement = spy(playerManagement);
        doReturn(game).when(spyPlayerManagement)
                      .getGame("lobbyId");
        doReturn(player).when(spyPlayerManagement)
                        .getPlayerByUser(user, game);

        IllegalGameStateException thrown = assertThrows(
                IllegalGameStateException.class,
                () -> spyPlayerManagement.getCardsToSort("lobbyId", user),
                "Expected getCardsToSort() to throw, but it did not"
        );

        assertTrue(thrown.getMessage()
                         .contains("The game´s current state is not playerturnstate"));
    }

    @Test
    void testDetermineRegionsForNurse() {
        ICity oldPosition = mock(ICity.class);
        ICity newPosition = mock(ICity.class);
        when(player.getGameId()).thenReturn("123");

        List<IRegionDTO> regions = playerManagement.determineRegionsForNurse(player, oldPosition, newPosition);

        assertNotNull(regions);
    }

    @Test
    void testPlacePreventionMarker() {
        playerManagement.placePreventionMarker("123", 1);
        assertTrue(game.getRegionRepository().getRegionByID(1).isPreventionMarker(), "Expected prevention marker to " +
                "be set to true");
    }
}
