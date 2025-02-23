package de.uol.swp.common.cards.data;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for various card types.
 */
class CardTest {

    /**
     * Tests the CityCardDTO class.
     */
    @Test
    void testCityCard() {
        ICityDTO city = createTestCity();
        CityCardDTO cityCard = new CityCardDTO(0, "cityCard", city);
        assertCardProperties(cityCard, 0, "cityCard", CardType.CITY_CARD);
        assertEquals(city, cityCard.getCity());
    }

    /**
     * Tests the EpidemicCardDTO class.
     */
    @Test
    void testEpidemicCard() {
        EpidemicCardDTO epidemicCard = new EpidemicCardDTO(1, "epidemicCard", "description");
        assertCardProperties(epidemicCard, 1, "epidemicCard", CardType.EPIDEMIC_CARD);
        assertEquals("description", epidemicCard.getDescription());
    }

    /**
     * Tests the EventCardDTO class.
     */
    @Test
    void testEventCard() {
        EventCardDTO eventCard = new EventCardDTO(1, "eventCard", "action");
        assertCardProperties(eventCard, 1, "eventCard", CardType.EVENT_CARD);
        assertEquals("action", eventCard.getAction());
    }

    /**
     * Tests the InfectionCardDTO class.
     */
    @Test
    void testInfectionCard() {
        ICityDTO city = createTestCity();
        InfectionCardDTO infectionCard = new InfectionCardDTO(1, "infectionCard", city);
        assertCardProperties(infectionCard, 1, "infectionCard", CardType.INFECTION_CARD);
        assertEquals(city, infectionCard.getCity());
    }

    /**
     * Creates a test city with predefined properties.
     *
     * @return a new instance of {@link ICityDTO} representing the test city.
     */
    private ICityDTO createTestCity() {
        return new CityDTO(1, PlagueName.CHOLERA, CityName.BARCELONA, 1234, true, false, new ArrayList<>());
    }

    /**
     * Asserts the properties of a card.
     *
     * @param card the card to be tested.
     * @param expectedId the expected ID of the card.
     * @param expectedTitle the expected title of the card.
     * @param expectedType the expected type of the card.
     */
    private void assertCardProperties(ICardDTO card, int expectedId, String expectedTitle, CardType expectedType) {
        assertEquals(expectedId, card.getId());
        assertEquals(expectedTitle, card.getTitle());
        assertEquals(expectedType, card.getType());
    }
}