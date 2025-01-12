package de.uol.swp.common.game.dto;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.cards.InfectionCardDTO;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.IConnectionDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.region.IRegionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GameDTO implements IGameDTO {
    private String gameId;
    private List<ICityDTO> cities;
    private List<IConnectionDTO> connections;
    private List<IRegionDTO> regions;
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
}

