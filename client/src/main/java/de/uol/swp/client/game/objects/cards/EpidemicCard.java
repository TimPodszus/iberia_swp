package de.uol.swp.client.game.objects.cards;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Represents an EpidemicCard in the game.
 * This card extends the AbstractCard class and sets up the visual representation
 * of the card with specific styles and text elements.
 */
public class EpidemicCard extends AbstractCard {

    /**
     * Constructs an EpidemicCard with the specified id.
     *
     * @param id the unique identifier for the card
     */
    public EpidemicCard(int id) {
        super(id);
        this.setPrefSize(100, 150);
        this.setStyle("-fx-background-color: f7d48e;");

        // Create and style the text for the epidemic label
        Text epedemicText = new Text("Epedemie");
        StackPane epedemicStackPane = new StackPane();
        epedemicStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        epedemicStackPane.getChildren()
                         .add(epedemicText);

        // Set the position of the epidemic label
        AnchorPane.setTopAnchor(epedemicStackPane, 5.0);
        AnchorPane.setLeftAnchor(epedemicStackPane, 0.0);
        AnchorPane.setRightAnchor(epedemicStackPane, 0.0);

        // Create and style the text for the description
        Text descriptionText = new Text("Beschreibung");
        descriptionText.setFont(new Font(10));
        descriptionText.setWrappingWidth(78.0);
        descriptionText.setTextAlignment(TextAlignment.CENTER);
        StackPane descriptionStackPane = new StackPane();
        descriptionStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        descriptionStackPane.getChildren()
                            .add(descriptionText);

        // Set the position of the description text
        AnchorPane.setTopAnchor(descriptionStackPane, 25.0);
        AnchorPane.setLeftAnchor(descriptionStackPane, 0.0);
        AnchorPane.setRightAnchor(descriptionStackPane, 0.0);

        // Add the text elements to the card
        this.getChildren()
            .addAll(epedemicStackPane, descriptionStackPane);
    }
}
