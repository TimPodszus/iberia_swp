package de.uol.swp.common.connection.response;

import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for AvailableDestinationsResponse.
 */
public class AvailableDestinationsResponseTest {

    /**
     * Tests the AvailableDestinationsResponse constructor and getCities method.
     */
    @Test
    void testAvailableDestinationsResponse() {
        ICityDTO city = new CityDTO(1,
                PlagueName.CHOLERA,
                CityName.PALMA_DE_MALLORCA,
                1,
                true,
                false,
                new ArrayList<>()
        );
        List<ICardDTO> cards = new ArrayList<>(List.of(new CityCardDTO(1,
                CityName.PALMA_DE_MALLORCA.getDisplayName(),
                city
        )));
        Map<Integer, List<ICardDTO>> destinations = Map.of(1, cards);
        AvailableDestinationsResponse availableDestinationsResponse = new AvailableDestinationsResponse("test",
                destinations
        );

        assertEquals("test", availableDestinationsResponse.getLobbyId());
        assertEquals(destinations, availableDestinationsResponse.getCities());
    }
}
