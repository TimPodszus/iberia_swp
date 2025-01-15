package de.uol.swp.server.cards;

import de.uol.swp.common.cards.*;
import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.city.data.City;
import de.uol.swp.common.city.CityName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CardMapperTest {
    private CityCard cityCard;
    private EpidemicCard epidemicCard;
    private InfectionCard infectionCard;

    @BeforeEach
    void setUp() {
        City city = new City(1, PlagueName.MALARIA, CityName.A_CORUNA, 1000, true, false, new ArrayList<>());
        cityCard = new CityCard(101, "City of London", CardType.CITY_CARD, city);
        epidemicCard = new EpidemicCard(202, "Severe Epidemic", CardType.EPIDEMIC_CARD, "Spreads rapidly.");
        infectionCard = new InfectionCard(303, "Infection in Paris", CardType.INFECTION_CARD, city);
    }

    @Test
    void testToCityCardDTO() {
        CityCardDTO cityCardDTO = CardMapper.toCityCardDTO(cityCard);

        assertNotNull(cityCardDTO);
        assertEquals(101, cityCardDTO.getId());
        assertEquals("City of London", cityCardDTO.getTitle());
        assertEquals(CardType.CITY_CARD, cityCardDTO.getType());

        CityDTO cityDTO = cityCardDTO.getCity();
        assertNotNull(cityDTO);
        assertEquals(1, cityDTO.getId());
        assertEquals(PlagueName.MALARIA, cityDTO.getPlagueName());
        assertEquals(CityName.A_CORUNA, cityDTO.getName());
        assertEquals(1000, cityDTO.getFoundationDate());
        assertTrue(cityDTO.isHarbourCity());
        assertFalse(cityDTO.isHospitalBuild());
        assertTrue(cityDTO.getInfections()
                          .isEmpty());
    }

    @Test
    void testToEpidemicCardDTO() {
        EpidemicCardDTO epidemicCardDTO = CardMapper.toEpidemicCardDTO(epidemicCard);

        assertNotNull(epidemicCardDTO);
        assertEquals(202, epidemicCardDTO.getId());
        assertEquals("Severe Epidemic", epidemicCardDTO.getTitle());
        assertEquals(CardType.EPIDEMIC_CARD, epidemicCardDTO.getType());
        assertEquals("Spreads rapidly.", epidemicCardDTO.getDescription());
    }

    @Test
    void testToInfectionCardDTO() {
        InfectionCardDTO infectionCardDTO = CardMapper.toInfectionCardDTO(infectionCard);

        assertNotNull(infectionCardDTO);
        assertEquals(303, infectionCardDTO.getId());
        assertEquals("Infection in Paris", infectionCardDTO.getTitle());
        assertEquals(CardType.INFECTION_CARD, infectionCardDTO.getType());

        CityDTO cityDTO = infectionCardDTO.getCity();
        assertNotNull(cityDTO);
        assertEquals(1, cityDTO.getId());
        assertEquals(PlagueName.MALARIA, cityDTO.getPlagueName());
        assertEquals(CityName.A_CORUNA, cityDTO.getName());
        assertEquals(1000, cityDTO.getFoundationDate());
        assertTrue(cityDTO.isHarbourCity());
        assertFalse(cityDTO.isHospitalBuild());
        assertTrue(cityDTO.getInfections()
                          .isEmpty());
    }

    @Test
    void testToDTOWithUnsupportedCardType() {
        Card unsupportedCard = mock(Card.class);

        ICardDTO result = CardMapper.toDTO(unsupportedCard);

        assertNull(result, "Expected toDTO to return null for unsupported card type.");
    }

    @Test
    void testToMixedCardDTOList() {
        List<ICardDTO> mixedDTOs = CardMapper.toMixedCardDTOList(List.of(cityCard, epidemicCard, infectionCard));

        assertEquals(3, mixedDTOs.size());
        assertNotNull(mixedDTOs.get(0));
        assertNotNull(mixedDTOs.get(1));
        assertNotNull(mixedDTOs.get(2));
    }

    @Test
    void testToCityCardDTOList() {
        CityCard cityCard1 = new CityCard(101,
                "Test",
                CardType.CITY_CARD,
                new City(1, PlagueName.MALARIA, CityName.A_CORUNA, 1000, true, false, new ArrayList<>())
        );
        CityCard cityCard2 = new CityCard(102,
                "Test",
                CardType.CITY_CARD,
                new City(2, PlagueName.MALARIA, CityName.ALICANTE, 2000, false, true, new ArrayList<>())
        );

        List<CityCardDTO> cityCardDTOs = CardMapper.toCityCardDTOList(List.of(cityCard1, cityCard2));

        assertEquals(2, cityCardDTOs.size());
        assertNotNull(cityCardDTOs.get(0));
        assertNotNull(cityCardDTOs.get(1));
    }

    @Test
    void testToEpidemicCardDTOList() {
        EpidemicCard epidemicCard1 = new EpidemicCard(1, "Test", CardType.EPIDEMIC_CARD, "Test");
        EpidemicCard epidemicCard2 = new EpidemicCard(1, "Test", CardType.EPIDEMIC_CARD, "Test");

        List<EpidemicCardDTO> epidemicCardDTOs = CardMapper.toEpidemicCardDTOList(List.of(
                epidemicCard1,
                epidemicCard2
        ));

        assertEquals(2, epidemicCardDTOs.size());
        assertNotNull(epidemicCardDTOs.get(0));
        assertNotNull(epidemicCardDTOs.get(1));
    }

    @Test
    void testToInfectionCardDTOList() {
        InfectionCard infectionCard1 = new InfectionCard(
                1,
                "Test",
                CardType.INFECTION_CARD,
                new City(1, PlagueName.MALARIA, CityName.A_CORUNA, 1000, true, false, new ArrayList<>())
        );
        InfectionCard infectionCard2 = new InfectionCard(
                1,
                "Test",
                CardType.INFECTION_CARD,
                new City(2, PlagueName.MALARIA, CityName.ALICANTE, 2000, false, true, new ArrayList<>())
        );


        List<InfectionCardDTO> infectionCardDTOs = CardMapper.toInfectionCardDTOList(List.of(
                infectionCard1,
                infectionCard2
        ));

        assertEquals(2, infectionCardDTOs.size());
        assertNotNull(infectionCardDTOs.get(0));
        assertNotNull(infectionCardDTOs.get(1));
    }
}
