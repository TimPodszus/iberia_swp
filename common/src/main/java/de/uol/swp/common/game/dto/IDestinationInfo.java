package de.uol.swp.common.game.dto;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.TransportMode;

import java.util.List;

/**
 * Interface representing destination information in the game.
 */
public interface IDestinationInfo {

    /**
     * Retrieves a list of cards that can be used for a move.
     *
     * @return A list of ICardDTO objects representing the usable cards.
     */
    List<ICardDTO> getCardsUsableForMove();

    /**
     * Retrieves the transport mode for the destination.
     *
     * @return A list of the transport modes for the destination.
     */
    List<TransportMode> getTransportModes();

    /**
     * Sets the list of cards that can be used for a move.
     *
     * @param cardsUsableForMove A list of ICardDTO objects representing the usable cards.
     */
    void setCardsUsableForMove(List<ICardDTO> cardsUsableForMove);

    /**
     * Adds a transport mode to the destination.
     *
     * @param transportMode The transport mode to be added.
     */
    void addTransportMode(TransportMode transportMode);
}
