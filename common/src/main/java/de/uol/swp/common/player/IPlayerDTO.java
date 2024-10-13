package de.uol.swp.common.player;

import de.uol.swp.common.cards.CardDTO;
import de.uol.swp.common.city.CityDTO;

import java.util.List;

public interface IPlayerDTO {
    String getUsername();

    String getRoleName();

    CityDTO getCurrentPosition();

    List<CardDTO> getCards();
}

