package de.uol.swp.common.player;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.role.IRoleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class PlayerDTO implements IPlayerDTO, Serializable {
    private final String username;
    private final IRoleDTO role;
    private final ICityDTO currentPosition;
    private final List<ICardDTO> cards;
}

