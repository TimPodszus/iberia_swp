package de.uol.swp.server.cards;

import de.uol.swp.server.city.CityRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for CardRepository.
 */
public class CardRepositoryTest {
    /**
     * Tests the getCards method and verifies that the correct number of cards is returned.
     */
    @Test
    public void testGetCards() {
        CardRepository cardRepository = new CardRepository(new CityRepository());
        assertEquals(
                102,
                cardRepository.getCards()
                              .size()
        );
    }
}
