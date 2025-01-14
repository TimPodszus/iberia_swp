package de.uol.swp.client.game.objects.cards;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class EpidemicCard extends AbstractCard {
    public EpidemicCard() {
        this.setPrefSize(100, 150);
        this.setStyle("-fx-background-color: f7d48e;");

        Text epedemicText = new Text("Epedemie");
        StackPane epedemicStackPane = new StackPane();
        epedemicStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        epedemicStackPane.getChildren().add(epedemicText);

        AnchorPane.setTopAnchor(epedemicStackPane, 5.0);
        AnchorPane.setLeftAnchor(epedemicStackPane, 0.0);
        AnchorPane.setRightAnchor(epedemicStackPane, 0.0);

        Text descriptionText = new Text("Beschreibung");
        descriptionText.setFont(new Font(10));
        descriptionText.setWrappingWidth(78.0);
        descriptionText.setTextAlignment(TextAlignment.CENTER);
        StackPane descriptionStackPane = new StackPane();
        descriptionStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        descriptionStackPane.getChildren().add(descriptionText);

        AnchorPane.setTopAnchor(descriptionStackPane, 25.0);
        AnchorPane.setLeftAnchor(descriptionStackPane, 0.0);
        AnchorPane.setRightAnchor(descriptionStackPane, 0.0);

        this.getChildren().addAll(epedemicStackPane, descriptionStackPane);
    }
}
