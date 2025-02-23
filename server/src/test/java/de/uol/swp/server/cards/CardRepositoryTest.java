package de.uol.swp.server.cards;

import de.uol.swp.server.city.CityRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardRepositoryTest {
    @Test
    public void testGetCards() {
        CardRepository cardRepository = new CardRepository(new CityRepository());
        assertEquals(104, cardRepository.getCards().size());
    }
}
