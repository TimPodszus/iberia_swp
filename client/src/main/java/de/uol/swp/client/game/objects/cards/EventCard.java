package de.uol.swp.client.game.objects.cards;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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

        // Create and style the event text
        Text eventText = new Text("Ereignis");
        eventText.setFont(new Font(10));
        StackPane eventStackPane = new StackPane();
        eventStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        eventStackPane.getChildren()
                      .add(eventText);

        // Position the event text
        AnchorPane.setTopAnchor(eventStackPane, 5.0);
        AnchorPane.setLeftAnchor(eventStackPane, 0.0);
        AnchorPane.setRightAnchor(eventStackPane, 0.0);

        // Create and style the event name text
        Text eventNameText = new Text(eventName);
        StackPane eventNameStackPane = new StackPane();
        eventNameStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        eventNameStackPane.getChildren()
                          .add(eventNameText);

        // Position the event name text
        AnchorPane.setTopAnchor(eventNameStackPane, 15.0);
        AnchorPane.setLeftAnchor(eventNameStackPane, 0.0);
        AnchorPane.setRightAnchor(eventNameStackPane, 0.0);

        // Create and style the image view
        ImageView imageView = new ImageView();
        imageView.setFitHeight(45.0);
        imageView.setFitWidth(78.0);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);

        // Position the image view
        AnchorPane.setTopAnchor(imageView, 32.0);
        AnchorPane.setBottomAnchor(imageView, 73.0);
        AnchorPane.setLeftAnchor(imageView, 11.0);
        AnchorPane.setRightAnchor(imageView, 11.0);

        // Create and style the description text
        Text descriptionText = new Text(description);
        descriptionText.setFont(new Font(10));
        descriptionText.setWrappingWidth(78.0);
        descriptionText.setTextAlignment(TextAlignment.CENTER);
        StackPane descriptionStackPane = new StackPane();
        descriptionStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        descriptionStackPane.getChildren()
                            .add(descriptionText);

        // Position the description text
        AnchorPane.setTopAnchor(descriptionStackPane, 75.0);
        AnchorPane.setLeftAnchor(descriptionStackPane, 0.0);
        AnchorPane.setRightAnchor(descriptionStackPane, 0.0);

        // Add all elements to the card
        this.getChildren()
            .addAll(eventStackPane, eventNameStackPane, imageView, descriptionStackPane);
    }
}
