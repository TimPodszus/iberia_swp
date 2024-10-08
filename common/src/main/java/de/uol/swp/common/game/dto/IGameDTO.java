package de.uol.swp.common.game.dto;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connectiom.IConnectionDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.cards.CardDTO;
import de.uol.swp.common.cards.InfectionCardDTO;
import de.uol.swp.common.region.IRegionDTO;
import java.util.List;

public interface IGameDTO {
    String getGameId();
    List<ICityDTO> getCities();
    List<IConnectionDTO> getConnections();
    List<IRegionDTO> getRegions();
    List<InfectionCardDTO> getInfectionCardDrawPile();
    List<InfectionCardDTO> getInfectionCardDiscardPile();
    List<CardDTO> getPlayerCardDrawPile();
    List<CardDTO> getPlayerCardDiscardPile();
    List<IPlayerDTO> getPlayers();
    int getInfectionCounter();
    int getEscalationStage();
    int getWaterTreatmentsLeft();
    int getTracksLeft();
    int getCurrentPlayerIndex();
}

