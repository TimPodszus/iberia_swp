package de.uol.swp.client.game;

import de.uol.swp.client.game.objects.cards.*;
import de.uol.swp.common.cards.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Factory class for creating different types of cards.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardFactory {

    /**
     * Creates an abstract card from the given card data.
     *
     * @param card the card data
     * @return the created abstract card
     */
    public static AbstractCard createCard(ICardDTO card) {
        if (card instanceof CityCardDTO cityCard) {
            return createCityCard(cityCard);
        } else if (card instanceof EpidemicCardDTO) {
            return new EpidemicCard(card.getId());
        } else if (card instanceof EventCardDTO eventCard) {
            return new EventCard(card.getId(), eventCard.getTitle(), eventCard.getAction());
        } else if (card instanceof InfectionCardDTO infectionCardDTO) {
            return new InfectionCard(
                    card.getId(),
                    infectionCardDTO.getCity()
                                    .getPlagueName(),
                    infectionCardDTO.getCity()
                                    .getName()
                                    .getDisplayName()
            );
        } else {
            throw new IllegalArgumentException("Unknown card type.");
        }
    }

    /**
     * Creates a city card from the given city card data.
     *
     * @param cityCard the city card data
     * @return the created city card
     */
    private static CityCard createCityCard(CityCardDTO cityCard) {
        String foundationDate = cityCard.getCity()
                                        .getFoundationDate() < 0 ? cityCard.getCity()
                                                                           .getFoundationDate() + " v. Chr." : String.valueOf(
                cityCard.getCity()
                        .getFoundationDate());
        return new CityCard(
                cityCard.getId(),
                cityCard.getCity()
                        .getName()
                        .getDisplayName(),
                foundationDate,
                cityCard.getCity()
                        .getPlagueName()
        );
    }
}
