package de.uol.swp.client.game.objects.cards;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Represents an event card in the game.
 */
public class EventCard extends AbstractCard {

    /**
     * Constructs an EventCard with the specified id, event name, and description.
     *
     * @param id          the unique identifier for the card
     * @param eventName   the name of the event
     * @param description the description of the event
     */
    public EventCard(int id, String eventName, String description) {
        super(id);
        this.setPrefSize(100, 150);
        this.setStyle("-fx-background-color: f7d48e;");

        StackPane eventStackPane = createTextStackPane("Ereigniskarte", 100.0, 10);
        StackPane eventNameStackPane = createTextStackPane(eventName, 100.0, 10);
        StackPane descriptionStackPane = createTextStackPane(description, 100.0, 10);

        VBox textVBox = new VBox();
        textVBox.setPrefSize(100, 150);
        textVBox.getChildren()
                .addAll(eventStackPane, eventNameStackPane, descriptionStackPane);

        this.getChildren()
            .addAll(textVBox);
    }
}
