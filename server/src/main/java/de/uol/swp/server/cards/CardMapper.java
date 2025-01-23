package de.uol.swp.server.cards;

import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.cards.EpidemicCardDTO;
import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.cards.InfectionCardDTO;
import de.uol.swp.common.city.CityDTO;
import de.uol.swp.server.infection.InfectionMapper;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Provides static methods to convert card objects into their respective Data Transfer Object (DTO) forms.
 * This class is part of the server-side cards package, which interacts with various card-related data.
 * It includes conversions for CityCards, EpidemicCards, and InfectionCards.
 */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CardMapper {
    /**
     * Converts a list of Card objects into a list of mixed CardDTOs.
     *
     * @param cards the list of Card objects to be converted
     * @return a List of CardDTO objects, each corresponding to the input list's specific card type
     */
    public static List<ICardDTO> toMixedCardDTOList(List<Card> cards) {
        return cards.stream()
                    .map(CardMapper::toDTO)
                    .toList();
    }

    /**
     * Converts a single Card object into its corresponding DTO form.
     * This method distinguishes between different subclasses of Card to route to the correct conversion method.
     *
     * @param card the Card object to convert
     * @return the CardDTO corresponding to the type of the provided Card object, or null if the type is not supported
     */
    public static ICardDTO toDTO(Card card) {
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

    /**
     * Converts a CityCard object into a CityCardDTO object.
     *
     * @param cityCard the CityCard to convert
     * @return a CityCardDTO object containing data from the provided CityCard and its associated City
     */
    static CityCardDTO toCityCardDTO(CityCard cityCard) {
        CityDTO cityDTO = new CityDTO(cityCard.getCity()
                                              .getId(),
                cityCard.getCity()
                        .getPlagueName(),
                cityCard.getCity()
                        .getName(),
                cityCard.getCity()
                        .getFoundationDate(),
                cityCard.getCity()
                        .isHarbourCity(),
                cityCard.getCity()
                        .isHospitalBuilt(),
                InfectionMapper.toDTOList(cityCard.getCity()
                                                  .getInfections())
        );
        return new CityCardDTO(cityCard.getId(), cityCard.getTitle(), cityCard.getType(), cityDTO);
    }

    /**
     * Converts an EpidemicCard into an EpidemicCardDTO.
     *
     * @param epidemicCard the EpidemicCard to convert
     * @return an EpidemicCardDTO containing data from the EpidemicCard
     */
    static EpidemicCardDTO toEpidemicCardDTO(EpidemicCard epidemicCard) {
        return new EpidemicCardDTO(epidemicCard.getId(), epidemicCard.getTitle(), epidemicCard.getDescription());
    }

    /**
     * Converts an InfectionCard into an InfectionCardDTO.
     *
     * @param infectionCard the InfectionCard to convert
     * @return an InfectionCardDTO containing data from the InfectionCard and its associated City
     */
    static InfectionCardDTO toInfectionCardDTO(InfectionCard infectionCard) {
        CityDTO cityDTO = new CityDTO(infectionCard.getCity()
                                                   .getId(),
                infectionCard.getCity()
                             .getPlagueName(),
                infectionCard.getCity()
                             .getName(),
                infectionCard.getCity()
                             .getFoundationDate(),
                infectionCard.getCity()
                             .isHarbourCity(),
                infectionCard.getCity()
                             .isHospitalBuilt(),
                InfectionMapper.toDTOList(infectionCard.getCity()
                                                       .getInfections())
        );
        return new InfectionCardDTO(infectionCard.getId(), infectionCard.getTitle(), infectionCard.getType(), cityDTO);
    }

    /**
     * Converts a list of CityCard objects into a list of CityCardDTOs.
     *
     * @param cityCards the list of CityCard objects to convert
     * @return a List of CityCardDTO objects
     */
    public static List<CityCardDTO> toCityCardDTOList(List<CityCard> cityCards) {
        return cityCards.stream()
                        .map(CardMapper::toCityCardDTO)
                        .toList();
    }

    /**
     * Converts a list of EpidemicCard objects into a list of EpidemicCardDTOs.
     *
     * @param epidemicCards the list of EpidemicCard objects to convert
     * @return a List of EpidemicCardDTO objects
     */
    public static List<EpidemicCardDTO> toEpidemicCardDTOList(List<EpidemicCard> epidemicCards) {
        return epidemicCards.stream()
                            .map(CardMapper::toEpidemicCardDTO)
                            .toList();
    }

    /**
     * Converts a list of InfectionCard objects into a list of InfectionCardDTOs.
     *
     * @param infectionCards the list of InfectionCard objects to convert
     * @return a List of InfectionCardDTO objects
     */
    public static List<InfectionCardDTO> toInfectionCardDTOList(List<InfectionCard> infectionCards) {
        return infectionCards.stream()
                             .map(CardMapper::toInfectionCardDTO)
                             .toList();
    }
}

