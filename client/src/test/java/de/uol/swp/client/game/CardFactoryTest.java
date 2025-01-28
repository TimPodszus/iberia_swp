package de.uol.swp.client.game;

import de.uol.swp.client.game.objects.cards.*;
import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.cards.EpidemicCardDTO;
import de.uol.swp.common.cards.EventCardDTO;
import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CardFactoryTest {
    @Test
    void testCityCardCreation() {
        ICityDTO cityDTO = new CityDTO(1, PlagueName.CHOLERA, CityName.BARCELONA, 1212, true, false, new ArrayList<>());
        CityCardDTO cityCardDTO = new CityCardDTO(
                1,
                cityDTO.getName()
                       .getDisplayName(),
                cityDTO
        );
        AbstractCard card = CardFactory.createCard(cityCardDTO);
        assertTrue(card instanceof CityCard);
        assertEquals(1, card.getCardId());
    }

    @Test
    void testInfectionCardCreation() {
        EpidemicCardDTO epidemicCardDTO = new EpidemicCardDTO(2, "Epidemic Title", "Epidemic Description");
        AbstractCard card = CardFactory.createCard(epidemicCardDTO);
        assertTrue(card instanceof EpidemicCard);
        assertEquals(2, card.getCardId());
    }

    @Test
    void testEventCardCreation() {
        EventCardDTO eventCardDTO = new EventCardDTO(3, "Event Title", "Event Action");
        AbstractCard card = CardFactory.createCard(eventCardDTO);
        assertTrue(card instanceof EventCard);
        assertEquals(3, card.getCardId());
    }

    @Test
    void testEpidemicCardCreation() {
        EpidemicCardDTO epidemicCardDTO = new EpidemicCardDTO(4, "Epidemic Title", "Epidemic Description");
        AbstractCard card = CardFactory.createCard(epidemicCardDTO);
        assertTrue(card instanceof EpidemicCard);
        assertEquals(4, card.getCardId());
    }
}
