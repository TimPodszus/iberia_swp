package de.uol.swp.client.game.objects.cards;

import de.uol.swp.common.game.PlagueName;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class InfectionCard extends AbstractCard {
    public InfectionCard(PlagueName plagueName, String city) {
        this.setPrefSize(150, 100);
        this.setStyle("-fx-background-color: " + plagueName.getColorCode() + ";");

        Text cityText = new Text(city);
        StackPane cityStackPane = new StackPane();
        cityStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        cityStackPane.getChildren().add(cityText);

        AnchorPane.setTopAnchor(cityStackPane, 10.0);
        AnchorPane.setLeftAnchor(cityStackPane, 0.0);
        AnchorPane.setRightAnchor(cityStackPane, 0.0);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(65.0);
        imageView.setFitWidth(130.0);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);

        AnchorPane.setTopAnchor(imageView, 25.0);
        AnchorPane.setBottomAnchor(imageView, 10.0);
        AnchorPane.setLeftAnchor(imageView, 10.0);
        AnchorPane.setRightAnchor(imageView, 10.0);

        this.getChildren().addAll(cityStackPane, imageView);
    }
}
