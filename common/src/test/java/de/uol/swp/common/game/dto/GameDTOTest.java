package de.uol.swp.common.game.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.cards.InfectionCardDTO;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connectiom.IConnectionDTO;
import de.uol.swp.common.game.StateType;
import de.uol.swp.common.plague.IPlagueDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.region.IRegionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class GameDTOTest {

    private GameDTO game;
    private List<ICityDTO> cities;
    private List<IConnectionDTO> connections;
    private List<IRegionDTO> regions;
    private List<IPlagueDTO> plagues;
    private List<InfectionCardDTO> infectionCardDrawPile;
    private List<InfectionCardDTO> infectionCardDiscardPile;
    private List<ICardDTO> playerCardDrawPile;
    private List<ICardDTO> playerCardDiscardPile;
    private List<IPlayerDTO> players;

    @BeforeEach
    void setUp() {
        cities = Collections.singletonList(mock(ICityDTO.class));
        connections = Collections.singletonList(mock(IConnectionDTO.class));
        regions = Collections.singletonList(mock(IRegionDTO.class));
        plagues = Collections.singletonList(mock(IPlagueDTO.class));
        infectionCardDrawPile = Collections.singletonList(mock(InfectionCardDTO.class));
        infectionCardDiscardPile = Collections.singletonList(mock(InfectionCardDTO.class));
        playerCardDrawPile = Collections.singletonList(mock(ICardDTO.class));
        playerCardDiscardPile = Collections.singletonList(mock(ICardDTO.class));
        players = Collections.singletonList(mock(IPlayerDTO.class));

        game = new GameDTO(
                "gameId",
                cities,
                connections,
                regions,
                plagues,
                infectionCardDrawPile,
                infectionCardDiscardPile,
                playerCardDrawPile,
                playerCardDiscardPile,
                players,
                1,
                1,
                2,
                3,
                0,
                StateType.START_STATE
        );
    }

    @Test
    void testConstructorInitialization() {
        assertNotNull(game.getCities());
        assertEquals("gameId", game.getGameId());
        assertEquals(1, game.getInfectionCounter());
    }

    @Test
    void testGetterMethods() {
        assertEquals(cities, game.getCities());
        assertEquals(connections, game.getConnections());
        assertEquals(regions, game.getRegions());
    }

    @Test
    void testEmptyLists() {
        GameDTO emptyGame = new GameDTO(
                "gameId",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                0,
                0,
                0,
                0,
                0,
                StateType.START_STATE
        );
        assertTrue(emptyGame.getCities()
                            .isEmpty());
    }
}
