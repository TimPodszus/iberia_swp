package de.uol.swp.common.game.dto;

import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.cards.data.InfectionCardDTO;
import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.dto.ConnectionDTO;
import de.uol.swp.common.connection.dto.IConnectionDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.StateType;
import de.uol.swp.common.plague.dto.IPlagueDTO;
import de.uol.swp.common.plague.dto.PlagueDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.player.PlayerDTO;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.region.RegionDTO;
import de.uol.swp.common.role.RoleDTO;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameDTOTest {

    private final ICityDTO palma = new CityDTO(1,
            PlagueName.CHOLERA,
            CityName.PALMA_DE_MALLORCA,
            12,
            true,
            true,
            List.of()
    );
    private final ICityDTO madrid = new CityDTO(2, PlagueName.CHOLERA, CityName.MADRID, 12, true, true, List.of());
    private final List<ICityDTO> cities = List.of(palma, madrid);
    private final List<IConnectionDTO> connections = List.of(new ConnectionDTO(1, List.of(), true, true));
    private final List<IRegionDTO> regions = List.of(new RegionDTO(1, List.of(), 1, true));
    private final List<IPlagueDTO> plague = List.of(new PlagueDTO(PlagueName.CHOLERA, 1, false));
    private final List<InfectionCardDTO> infectionCardDrawPile = List.of(new InfectionCardDTO(1,
            "Infection in Palma",
            palma
    ));
    private final List<InfectionCardDTO> infectionCardDiscardPile = List.of(new InfectionCardDTO(2,
            "Infection in " + "Madrid",
            madrid
    ));
    private final List<ICardDTO> playerCardDrawPile = List.of(new CityCardDTO(1, "palma", palma));
    private final List<ICardDTO> playerCardDiscardPile = List.of(new CityCardDTO(2, "madrid", madrid));
    private final List<IPlayerDTO> players = List.of(new PlayerDTO("player1",
            new RoleDTO(RoleEnum.NURSE, ""),
            palma,
            List.of()
    ));
    private final int infectionCounter = 5;
    private final int escalationStage = 5;
    private final int waterTreatmentsLeft = 5;
    private final int tracksLeft = 5;
    private final int currentPlayerIndex = 0;

    @Test
    void testConstructor() {
        IGameDTO gameDTO = createTestGameDTO("testGame");

        assertEquals("testGame", gameDTO.getGameId(), "GameId is not set correctly");
        assertEquals(cities, gameDTO.getCities(), "Cities are not set correctly");
        assertEquals(connections, gameDTO.getConnections(), "Connections are not set correctly");
        assertEquals(regions, gameDTO.getRegions(), "Regions are not set correctly");
        assertEquals(plague, gameDTO.getPlagues(), "Plagues are not set correctly");
        assertEquals(infectionCardDrawPile,
                gameDTO.getInfectionCardDrawPile(),
                "InfectionCardDrawPile is not set " + "correctly"
        );
        assertEquals(infectionCardDiscardPile,
                gameDTO.getInfectionCardDiscardPile(),
                "InfectionCardDiscardPile is not " + "set correctly"
        );
        assertEquals(playerCardDrawPile, gameDTO.getPlayerCardDrawPile(), "PlayerCardDrawPile is not set correctly");
        assertEquals(playerCardDiscardPile,
                gameDTO.getPlayerCardDiscardPile(),
                "PlayerCardDiscardPile is not set " + "correctly"
        );
        assertEquals(players, gameDTO.getPlayers(), "Players are not set correctly");
        assertEquals(infectionCounter, gameDTO.getInfectionCounter(), "InfectionCounter is not set correctly");
        assertEquals(escalationStage, gameDTO.getEscalationStage(), "EscalationStage is not set correctly");
        assertEquals(waterTreatmentsLeft, gameDTO.getWaterTreatmentsLeft(), "WaterTreatmentsLeft is not set correctly");
        assertEquals(tracksLeft, gameDTO.getTracksLeft(), "TracksLeft is not set correctly");
        assertEquals(currentPlayerIndex, gameDTO.getCurrentPlayerIndex(), "CurrentPlayerIndex is not set correctly");
    }

    @Test
    void testEquals() {
        IGameDTO gameDTO1 = createTestGameDTO("testGameEquals");
        IGameDTO gameDTO2 = createTestGameDTO("testGameEquals");

        assertEquals(gameDTO1, gameDTO2, "GameDTOs are not equal");
    }

    @Test
    void testEqualsWithSameObject() {
        IGameDTO gameDTO = createTestGameDTO("testGameEqualsWithSameObject");

        assertEquals(gameDTO, gameDTO, "GameDTO is not equal to itself");
    }

    @Test
    void testEqualsWithOtherClass() {
        IGameDTO gameDTO = createTestGameDTO("testGameEqualsWithOtherClass");

        assertNotEquals(new Object(), gameDTO);
    }

    @Test
    void testHashCode() {
        IGameDTO gameDTO1 = createTestGameDTO("testGame");
        IGameDTO gameDTO2 = createTestGameDTO("testGame");

        assertEquals(gameDTO1.hashCode(), gameDTO2.hashCode(), "HashCodes are not equal");
    }

    @Test
    void testConstructorInitialization() {
        IGameDTO game = createTestGameDTO("gameId");
        assertNotNull(game.getCities());
        assertEquals("gameId", game.getGameId());
        assertEquals(5, game.getInfectionCounter());
    }

    @Test
    void testGetterMethods() {
        IGameDTO game = createTestGameDTO("gameId");
        assertEquals(cities, game.getCities());
        assertEquals(connections, game.getConnections());
        assertEquals(regions, game.getRegions());
    }

    @Test
    void testEmptyLists() {
        GameDTO emptyGame = new GameDTO("gameId",
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

    @Test
    void testGetCurrentPlayer() {
        IGameDTO game = createTestGameDTO("gameId");
        assertEquals(players.get(currentPlayerIndex), game.getCurrentPlayer());
    }

    @Test
    void testGetPlayer() {
        IGameDTO game = createTestGameDTO("gameId");
        assertEquals(players.get(0), game.getPlayer("player1"));
    }

    private IGameDTO createTestGameDTO(String gameId) {
        return new GameDTO(gameId,
                cities,
                connections,
                regions,
                plague,
                infectionCardDrawPile,
                infectionCardDiscardPile,
                playerCardDrawPile,
                playerCardDiscardPile,
                players,
                infectionCounter,
                escalationStage,
                waterTreatmentsLeft,
                tracksLeft,
                currentPlayerIndex,
                StateType.START_STATE
        );
    }
}
