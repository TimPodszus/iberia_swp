package de.uol.swp.common.cards.data;

/**
 * Interface representing a Data Transfer Object (DTO) for a card.
 */
public interface ICardDTO {

    /**
     * Gets the unique identifier of the card.
     *
     * @return the unique identifier of the card
     */
    int getId();

    /**
     * Gets the title of the card.
     *
     * @return the title of the card
     */
    String getTitle();

    /**
     * Gets the type of the card.
     *
     * @return the type of the card
     */
    CardType getType();
}