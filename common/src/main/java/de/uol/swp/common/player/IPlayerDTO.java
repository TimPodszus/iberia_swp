package de.uol.swp.common.player;

import de.uol.swp.common.cards.CardDTO;
import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.role.IRoleDTO;

import java.util.List;

public interface IPlayerDTO {
    String getUsername();

    IRoleDTO getRole();

    CityDTO getCurrentPosition();

    List<CardDTO> getCards();
}

