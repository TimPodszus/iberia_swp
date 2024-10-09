package de.uol.swp.client.game.objects.cards;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class EpidemicCard extends AbstractCard {
    public EpidemicCard() {
        this.setPrefSize(100, 150);
        this.setStyle("-fx-background-color: f7d48e;");

        Text epedemicText = new Text("Epedemie");
        AnchorPane.setTopAnchor(epedemicText, 7.05);
        epedemicText.setLayoutX(24.0);
        epedemicText.setLayoutY(20.0);

        Text descriptionText = new Text("Beschreibung");
        descriptionText.setFont(new Font(10));
        descriptionText.setWrappingWidth(78.0);
        descriptionText.setLayoutX(11.0);
        descriptionText.setLayoutY(44.0);

        this.getChildren().addAll(epedemicText, descriptionText);
    }
}
