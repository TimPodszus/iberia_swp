package de.uol.swp.common.connection.dto;

import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.TransportMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DestinationInfoTest {

    private DestinationInfo destinationInfo;

    @BeforeEach
    void setUp() {
        destinationInfo = new DestinationInfo(new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void testAddTransportMode() {
        assertTrue(destinationInfo.getTransportModes().isEmpty());

        destinationInfo.addTransportMode(TransportMode.SHIP);

        List<TransportMode> modes = destinationInfo.getTransportModes();
        assertEquals(1, modes.size());
        assertEquals(TransportMode.SHIP, modes.get(0));
    }

    @Test
    void testSetCardsUsableForMove() {
        List<ICardDTO> dummyCards = new ArrayList<>();
        dummyCards.add(new CityCardDTO(1, "TestCity", null));

        destinationInfo.setCardsUsableForMove(dummyCards);

        assertEquals(dummyCards, destinationInfo.getCardsUsableForMove());
    }
}
