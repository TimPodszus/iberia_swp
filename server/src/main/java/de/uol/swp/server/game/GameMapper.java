package de.uol.swp.server.game;

import de.uol.swp.common.game.dto.GameDTO;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.city.CityMapper;
import de.uol.swp.server.connection.ConnectionMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.plague.PlagueMapper;
import de.uol.swp.server.player.PlayerMapper;
import de.uol.swp.server.region.RegionMapper;
import lombok.NoArgsConstructor;

/**
 * Utility class for mapping game-related objects to their Data Transfer Object (DTO) forms.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class GameMapper {
    /**
     * Converts an IGame object to an IGameDTO object.
     *
     * @param game the IGame object to convert
     * @return the converted IGameDTO object
     */
    public static IGameDTO toDTO(IGame game) {
        return new GameDTO(
                game.getGameId(),
                CityMapper.toDTOList(game.getCityRepository()
                                         .getCities()),
                ConnectionMapper.toDTOList(game.getConnectionRepository()
                                               .getConnections()),
                RegionMapper.toDTOList(game.getRegionRepository()
                                           .getRegions()),
                PlagueMapper.toDTOList(game.getPlagueRepository()
                                           .getPlagues()),
                CardMapper.toInfectionCardDTOList(game.getInfectionCardDrawPile()),
                CardMapper.toInfectionCardDTOList(game.getInfectionCardDiscardPile()),
                CardMapper.toMixedCardDTOList(game.getPlayerCardDrawPile()),
                CardMapper.toMixedCardDTOList(game.getPlayerCardDiscardPile()),
                PlayerMapper.toDTOList(game.getPlayers()),
                game.getInfectionCounter(),
                game.getEscalationStage(),
                game.getWaterTreatmentsLeft(),
                game.getTracksLeft(),
                game.getCurrentPlayerIndex(),
                game.getState().getClass().getSimpleName()
        );
    }
}
