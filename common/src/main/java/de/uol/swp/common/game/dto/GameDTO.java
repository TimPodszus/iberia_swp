package de.uol.swp.common.game.dto;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.cards.InfectionCardDTO;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.IConnectionDTO;
import de.uol.swp.common.plague.IPlagueDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.region.IRegionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class GameDTO implements IGameDTO, Serializable {
    private String gameId;
    private List<ICityDTO> cities;
    private List<IConnectionDTO> connections;
    private List<IRegionDTO> regions;
    private List<IPlagueDTO> plagues;
    private List<InfectionCardDTO> infectionCardDrawPile;
    private List<InfectionCardDTO> infectionCardDiscardPile;
    private List<ICardDTO> playerCardDrawPile;
    private List<ICardDTO> playerCardDiscardPile;
    private List<IPlayerDTO> players;
    private int infectionCounter;
    private int escalationStage;
    private int waterTreatmentsLeft;
    private int tracksLeft;
    private int currentPlayerIndex;
    private String state;

    public IPlayerDTO getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        GameDTO gameDTO = (GameDTO) object;
        return infectionCounter == gameDTO.infectionCounter && escalationStage == gameDTO.escalationStage && waterTreatmentsLeft == gameDTO.waterTreatmentsLeft && tracksLeft == gameDTO.tracksLeft && currentPlayerIndex == gameDTO.currentPlayerIndex && Objects.equals(gameId,
                gameDTO.gameId
        ) && Objects.equals(cities, gameDTO.cities) && Objects.equals(connections,
                gameDTO.connections
        ) && Objects.equals(regions, gameDTO.regions) && Objects.equals(
                plagues,
                gameDTO.plagues
        ) && Objects.equals(infectionCardDrawPile, gameDTO.infectionCardDrawPile) && Objects.equals(
                infectionCardDiscardPile,
                gameDTO.infectionCardDiscardPile
        ) && Objects.equals(playerCardDrawPile, gameDTO.playerCardDrawPile) && Objects.equals(
                playerCardDiscardPile,
                gameDTO.playerCardDiscardPile
        ) && Objects.equals(players, gameDTO.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId,
                cities,
                connections,
                regions,
                plagues,
                infectionCardDrawPile,
                infectionCardDiscardPile,
                playerCardDrawPile,
                playerCardDiscardPile,
                players,
                infectionCounter,
                escalationStage,
                waterTreatmentsLeft,
                tracksLeft,
                currentPlayerIndex
        );
    }
}

