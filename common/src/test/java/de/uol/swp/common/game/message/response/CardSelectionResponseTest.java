package de.uol.swp.common.game.message.response;

import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.CardSelectionType;
import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardSelectionResponseTest {
    @Test
    void testConstructor() {
        ICityDTO cityDTO = new CityDTO(1, PlagueName.CHOLERA, CityName.BARCELONA, 1212, true, false, new ArrayList<>());
        List<ICardDTO> cards = List.of(new CityCardDTO(1,
                cityDTO.getName()
                       .getDisplayName(),
                cityDTO
        ));
        CardSelectionResponse response = new CardSelectionResponse("lobbyId",
                true,
                cards,
                CardSelectionType.TOO_MUCH_CARDS_ON_HAND
        );

        assertEquals("lobbyId", response.getLobbyId());
        assertTrue(response.isSuccess());
        assertEquals(cards, response.getCards());
        assertTrue(response.isDismissible());
        assertEquals(CardSelectionType.TOO_MUCH_CARDS_ON_HAND, response.getType());
    }
}
