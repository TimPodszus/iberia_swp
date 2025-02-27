package de.uol.swp.client.game.objects.cards;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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

        StackPane epidemicStackPane = createTextStackPane("Epidemie", 100.0, 10);
        StackPane descriptionStackPane = createTextStackPane(
                "Infektionsquote wird erhöht. Eine Stadt wird mit drei Seuchenwürfeln infiziert. Der " +
                        "Infektionsablagestapel wird gemischt und oben auf den Ziehstapel gelegt.",
                100.0,
                10
        );

        VBox textVBox = new VBox(5);
        textVBox.setPrefSize(100, 150);
        textVBox.getChildren()
                .addAll(epidemicStackPane, descriptionStackPane);

        this.getChildren()
            .addAll(textVBox);
    }
}
