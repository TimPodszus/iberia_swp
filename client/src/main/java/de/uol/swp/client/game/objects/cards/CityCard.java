package de.uol.swp.client.game.objects.cards;

import de.uol.swp.common.game.PlagueName;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Represents a CityCard in the game.
 */
public class CityCard extends AbstractCard {

    /**
     * Constructs a CityCard with the specified id, city, year, and plague name.
     *
     * @param id         the unique identifier of the card
     * @param city       the name of the city
     * @param year       the year associated with the card
     * @param plagueName the name of the plague associated with the card
     */
    public CityCard(int id, String city, String year, PlagueName plagueName) {
        super(id);
        this.setPrefSize(100, 150);
        this.setStyle("-fx-background-color: " + plagueName.getColorCode() + ";");

        StackPane cityStackPane = createTextStackPane(city, 100.0, 10);
        StackPane yearStackPane = createTextStackPane(year, 100.0, 10);

        VBox textVBox = new VBox(5);
        textVBox.setPrefSize(100, 150);
        textVBox.getChildren()
                .addAll(cityStackPane, yearStackPane);

        this.getChildren()
            .addAll(textVBox);
    }
}