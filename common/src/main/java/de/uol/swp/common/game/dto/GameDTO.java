package de.uol.swp.common.game.dto;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connectiom.IConnectionDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.cards.CardDTO;
import de.uol.swp.common.cards.InfectionCardDTO;
import de.uol.swp.common.region.IRegionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class GameDTO implements IGameDTO, Serializable {
    private String gameId;
    private transient List<ICityDTO> cities;
    private transient List<IConnectionDTO> connections;
    private transient List<IRegionDTO> regions;
    private List<InfectionCardDTO> infectionCardDrawPile;
    private List<InfectionCardDTO> infectionCardDiscardPile;
    private List<CardDTO> playerCardDrawPile;
    private List<CardDTO> playerCardDiscardPile;
    private transient List<IPlayerDTO> players;
    private int infectionCounter;
    private int escalationStage;
    private int waterTreatmentsLeft;
    private int tracksLeft;
    private int currentPlayerIndex;
}

