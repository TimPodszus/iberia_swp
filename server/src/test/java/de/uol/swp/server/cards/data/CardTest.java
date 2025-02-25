package de.uol.swp.server.cards.data;

import de.uol.swp.common.cards.data.CardType;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.ICity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CardTest {
    @Test
    public void testCityCard() {
        ICity city = new City(1, PlagueName.CHOLERA, CityName.BARCELONA, 1234, false);
        CityCard cityCard = new CityCard(1, "title", city);

        assertEquals(1, cityCard.getId());
        assertEquals("title", cityCard.getTitle());
        assertEquals(CardType.CITY_CARD, cityCard.getType());
        assertEquals(city, cityCard.getCity());
    }

    @Test
    public void testEpidemicCard() {
        EpidemicCard epidemicCard = new EpidemicCard(1, "title", "description");

        assertEquals(1, epidemicCard.getId());
        assertEquals("title", epidemicCard.getTitle());
        assertEquals(CardType.EPIDEMIC_CARD, epidemicCard.getType());
        assertEquals("description", epidemicCard.getDescription());
    }

    @Test
    public void testInfectionCard() {
        ICity city = new City(1, PlagueName.CHOLERA, CityName.BARCELONA, 1234, false);
        InfectionCard infectionCard = new InfectionCard(1, "title", city);

        assertEquals(1, infectionCard.getId());
        assertEquals("title", infectionCard.getTitle());
        assertEquals(CardType.INFECTION_CARD, infectionCard.getType());
        assertEquals(city, infectionCard.getCity());
    }

    @Test
    public void testEquals() {
        ICard card = new EpidemicCard(1, "title", "description");
        ICard card2 = new EpidemicCard(1, "title", "description");

        assertEquals(card, card2);
    }

    @Test
    public void testNotEquals() {
        ICard card = new EpidemicCard(1, "title", "description");

        assertNotEquals(card, new Object());
    }

    @Test
    public void testEqualsWithSameObject() {
        ICard card = new EpidemicCard(1, "title", "description");

        assertEquals(card, card);
    }

    @Test
    public void testHashCode() {
        ICard card = new EpidemicCard(1, "title", "description");
        ICard card2 = new EpidemicCard(1, "title", "description");

        assertEquals(card.hashCode(), card2.hashCode());
    }
}
