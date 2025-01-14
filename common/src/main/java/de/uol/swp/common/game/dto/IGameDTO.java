package de.uol.swp.common.game.dto;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.cards.InfectionCardDTO;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.IConnectionDTO;
import de.uol.swp.common.plague.IPlagueDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.region.IRegionDTO;

import java.util.List;

public interface IGameDTO {
    String getGameId();

    List<ICityDTO> getCities();

    List<IConnectionDTO> getConnections();

    List<IRegionDTO> getRegions();

    List<IPlagueDTO> getPlagues();

    List<InfectionCardDTO> getInfectionCardDrawPile();

    List<InfectionCardDTO> getInfectionCardDiscardPile();

    List<ICardDTO> getPlayerCardDrawPile();

    List<ICardDTO> getPlayerCardDiscardPile();

    List<IPlayerDTO> getPlayers();

    int getInfectionCounter();

    int getEscalationStage();

    int getWaterTreatmentsLeft();

    int getTracksLeft();

    int getCurrentPlayerIndex();
}

