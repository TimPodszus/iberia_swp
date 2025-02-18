package de.uol.swp.common.game.dto;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.TransportMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DestinationInfo {
    private List<ICardDTO> cardsUsableForMove;
    private TransportMode transportMode;
}
