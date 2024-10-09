package de.uol.swp.client.game.objects.cards;

import de.uol.swp.common.game.PlagueName;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CityCard extends AbstractCard {
    public CityCard(String city, String year, PlagueName plagueName) {
        this.setPrefSize(100, 150);
        this.setStyle("-fx-background-color: " + plagueName.getColorCode() + ";");

        Text cityText = new Text(city);
        StackPane cityStackPane = new StackPane();
        cityStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        cityStackPane.getChildren().add(cityText);

        AnchorPane.setTopAnchor(cityStackPane, 5.0);
        AnchorPane.setLeftAnchor(cityStackPane, 0.0);
        AnchorPane.setRightAnchor(cityStackPane, 0.0);

        Text yearText = new Text(year);
        yearText.setFont(new Font(10));
        StackPane yearStackPane = new StackPane();
        yearStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        yearStackPane.getChildren().add(yearText);

        AnchorPane.setTopAnchor(yearStackPane, 25.0);
        AnchorPane.setLeftAnchor(yearStackPane, 0.0);
        AnchorPane.setRightAnchor(yearStackPane, 0.0);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(95);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);

        AnchorPane.setBottomAnchor(imageView, 5.0);
        AnchorPane.setTopAnchor(imageView, 45.0);
        AnchorPane.setLeftAnchor(imageView, 10.0);
        AnchorPane.setRightAnchor(imageView, 10.0);

        this.getChildren()
            .addAll(cityStackPane, yearStackPane, imageView);
    }
}
