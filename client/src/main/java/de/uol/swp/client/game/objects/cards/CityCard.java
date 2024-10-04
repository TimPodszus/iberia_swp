package de.uol.swp.client.game.objects.cards;

import de.uol.swp.common.game.PlagueName;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CityCard extends AbstractCard {
    public CityCard(String city, String year, PlagueName plagueName) {
        this.setPrefSize(100, 150);
        this.setStyle("-fx-background-color: " + plagueName.getColorCode() + ";");

        Text cityText = new Text(city);
        AnchorPane.setLeftAnchor(cityText, 5.0);
        AnchorPane.setTopAnchor(cityText, 5.0);
        cityText.setLayoutX(61.0);
        cityText.setLayoutY(30.0);

        Text yearText = new Text(year);
        yearText.setFont(new Font(8));
        AnchorPane.setLeftAnchor(yearText, 10.0);
        AnchorPane.setTopAnchor(yearText, 25.0);

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
            .addAll(cityText, yearText, imageView);
    }
}
