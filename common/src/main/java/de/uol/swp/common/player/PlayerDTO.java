package de.uol.swp.common.player;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.city.ICityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PlayerDTO implements IPlayerDTO {
    private final String username;
    private final String roleName;
    private final ICityDTO currentPosition;
    private final List<ICardDTO> cards;
}

