package de.uol.swp.client.game;

import de.uol.swp.client.game.objects.cards.AbstractCard;
import de.uol.swp.client.game.objects.cards.CityCard;
import de.uol.swp.client.game.objects.cards.EpidemicCard;
import de.uol.swp.client.game.objects.cards.EventCard;
import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.cards.EpidemicCardDTO;
import de.uol.swp.common.cards.EventCardDTO;
import de.uol.swp.common.cards.ICardDTO;
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
     * @param playerCard the card data
     * @return the created abstract card
     */
    public static AbstractCard createCard(ICardDTO playerCard) {
        if (playerCard instanceof CityCardDTO cityCard) {
            return createCityCard(cityCard);
        } else if (playerCard instanceof EpidemicCardDTO) {
            return new EpidemicCard(playerCard.getId());
        } else if (playerCard instanceof EventCardDTO eventCard) {
            return new EventCard(playerCard.getId(), eventCard.getTitle(), eventCard.getAction());
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
