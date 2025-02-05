package de.uol.swp.server.cards;

import de.uol.swp.common.cards.CardType;

public interface ICard {
    /**
     * Gets the id of the card.
     *
     * @return the id of the card
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
