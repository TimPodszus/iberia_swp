package de.uol.swp.server.cards.data.eventcards;


import de.uol.swp.common.cards.data.CardType;
import de.uol.swp.server.cards.data.Card;
import lombok.Getter;
import org.greenrobot.eventbus.EventBus;

/**
 * Abstract class representing an event card in the game.
 * Extends the Card class and provides additional functionality specific to event cards.
 */
public abstract class EventCard extends Card {
    /**
     * The EventBus instance used for event handling.
     */
    protected final EventBus bus;

    /**
     * The description of the event card.
     */
    @Getter
    private final String description;

    /**
     * Constructs a new EventCard with the specified id and title.
     *
     * @param id          the unique identifier of the card
     * @param title       the title of the card
     * @param description the description of the card
     */
    protected EventCard(int id, String title, String description) {
        super(id, title, CardType.EVENT_CARD);
        this.description = description;
        this.bus = EventBus.getDefault();
    }

    /**
     * Executes the event card's action.
     *
     * @param lobbyId  the ID of the lobby where the action is executed
     * @param username the username of the player executing the action
     */
    public abstract void execute(String lobbyId, String username);
}
