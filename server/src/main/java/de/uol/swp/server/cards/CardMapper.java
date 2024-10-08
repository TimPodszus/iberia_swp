package de.uol.swp.server.cards;

import de.uol.swp.common.cards.CardDTO;
import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.cards.EpidemicCardDTO;
import de.uol.swp.common.cards.InfectionCardDTO;
import de.uol.swp.common.city.CityDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CardMapper {

    public static List<CardDTO> toMixedCardDTOList(List<Card> cards) {
        return cards.stream()
                    .map(CardMapper::toDTO)
                    .collect(Collectors.toList());
    }

    public static CardDTO toDTO(Card card) {
        if (card instanceof CityCard cityCard) {
            return toCityCardDTO(cityCard);
        } else if (card instanceof EpidemicCard epidemicCard) {
            return toEpidemicCardDTO(epidemicCard);
        } else if (card instanceof InfectionCard infectionCard) {
            return toInfectionCardDTO(infectionCard);
        } else {
          return null;
        }
    }

    private static CityCardDTO toCityCardDTO(CityCard cityCard) {
        CityDTO cityDTO = new CityDTO(cityCard.getCity().getPlagueName().toString(),
                cityCard.getCity()
                             .getName()
                             .getDisplayName(), cityCard.getCity().getFoundationDate(),
                cityCard.getCity().isHarbourCity(), cityCard.getCity().isHospitalBuilt()
        );
        return new CityCardDTO(cityCard.getId(), cityCard.getTitle(), cityCard.getType(), cityDTO);
    }

    private static EpidemicCardDTO toEpidemicCardDTO(EpidemicCard epidemicCard) {
        return new EpidemicCardDTO(
                epidemicCard.getId(),
                epidemicCard.getTitle(),
                epidemicCard.getType(),
                epidemicCard.getDescription()
        );
    }

    private static InfectionCardDTO toInfectionCardDTO(InfectionCard infectionCard) {
        CityDTO cityDTO = new CityDTO(infectionCard.getCity().getPlagueName().toString(),
                infectionCard.getCity()
                             .getName()
                             .getDisplayName(), infectionCard.getCity().getFoundationDate(),
                infectionCard.getCity().isHarbourCity(), infectionCard.getCity().isHospitalBuilt()
        );
        return new InfectionCardDTO(infectionCard.getId(), infectionCard.getTitle(), infectionCard.getType(), cityDTO);
    }
    public static List<CityCardDTO> toCityCardDTOList(List<CityCard> cityCards) {
        return cityCards.stream()
                        .map(CardMapper::toCityCardDTO)
                        .collect(Collectors.toList());
    }

    public static List<EpidemicCardDTO> toEpidemicCardDTOList(List<EpidemicCard> epidemicCards) {
        return epidemicCards.stream()
                            .map(CardMapper::toEpidemicCardDTO)
                            .collect(Collectors.toList());
    }

    public static List<InfectionCardDTO> toInfectionCardDTOList(List<InfectionCard> infectionCards) {
        return infectionCards.stream()
                             .map(CardMapper::toInfectionCardDTO)
                             .collect(Collectors.toList());
    }
}

