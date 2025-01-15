package de.uol.swp.common.player;

import de.uol.swp.common.role.IRoleDTO;
import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.city.ICityDTO;

import java.util.List;

public interface IPlayerDTO {
    String getUsername();

    IRoleDTO getRole();

    ICityDTO getCurrentPosition();

    List<ICardDTO> getCards();
}

