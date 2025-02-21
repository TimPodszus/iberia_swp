package de.uol.swp.common.game.dto;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.TransportMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class DestinationInfo implements IDestinationInfo, Serializable {
    @Setter
    private List<ICardDTO> cardsUsableForMove;
    private List<TransportMode> transportModes;

    @Override
    public void addTransportMode(TransportMode transportMode) {
        this.transportModes.add(transportMode);
    }
}
