package de.uol.swp.common.game.message.response;

import de.uol.swp.common.cards.CardType;
import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardExchangeResponseTest {

    @Test
    void testConstructor() {
        ICityDTO cityDTO = new CityDTO(1, PlagueName.CHOLERA, CityName.BARCELONA, 1212, true, false, new ArrayList<>());
        List<ICardDTO> cards = List.of(new CityCardDTO(
                1,
                cityDTO.getName()
                       .getDisplayName(),
                CardType.CITY_CARD,
                cityDTO
        ));
        Map<String, List<ICardDTO>> playerCards = Map.of("player", cards);
        CardExchangeResponse response = new CardExchangeResponse("lobbyId", true, playerCards);
        assertEquals("lobbyId", response.getLobbyId());
        assertTrue(response.isSuccess());
        assertEquals(playerCards, response.getPlayerCards());
    }
}
