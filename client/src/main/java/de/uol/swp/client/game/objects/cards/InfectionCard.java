package de.uol.swp.client.game.objects.cards;

import de.uol.swp.common.game.PlagueName;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Represents an infection card in the game.
 * This card displays the city name and an image associated with the infection.
 */
public class InfectionCard extends AbstractCard {

    /**
     * Constructs an InfectionCard with the specified id, plague name, and city.
     *
     * @param id         the unique identifier for the card
     * @param plagueName the name of the plague associated with the card
     * @param city       the name of the city displayed on the card
     */
    public InfectionCard(int id, PlagueName plagueName, String city) {
        super(id);
        this.setPrefSize(150, 100);
        this.setStyle("-fx-background-color: " + plagueName.getColorCode() + ";");

        StackPane cityStackPane = createTextStackPane(city, 150.0, 10);

        VBox textVBox = new VBox();
        textVBox.setPrefSize(150, 100);
        textVBox.getChildren()
                .addAll(cityStackPane);

        this.getChildren()
            .addAll(textVBox);
    }
}