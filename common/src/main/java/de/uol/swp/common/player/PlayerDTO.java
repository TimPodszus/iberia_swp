package de.uol.swp.common.player;

import de.uol.swp.common.cards.CardDTO;
import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.role.IRoleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PlayerDTO implements IPlayerDTO {
    private final String username;
    private final IRoleDTO role;
    private final CityDTO currentPosition;
    private final List<CardDTO> cards;
}

