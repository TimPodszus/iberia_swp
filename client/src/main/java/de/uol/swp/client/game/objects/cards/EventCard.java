package de.uol.swp.client.game.objects.cards;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class EventCard extends AbstractCard {
    public EventCard(String eventName, String description) {
        this.setPrefSize(100, 150);
        this.setStyle("-fx-background-color: f7d48e;");

        Text eventText = new Text("Ereignis");
        eventText.setFont(new Font(8));
        AnchorPane.setTopAnchor(eventText, 5.0);
        eventText.setLayoutX(36.0);
        eventText.setLayoutY(17.0);

        Text eventNameText = new Text(eventName);
        AnchorPane.setTopAnchor(eventNameText, 16.0);
        eventNameText.setLayoutX(15.0);
        eventNameText.setLayoutY(29.0);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(45.0);
        imageView.setFitWidth(78.0);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);

        AnchorPane.setTopAnchor(imageView, 32.0);
        AnchorPane.setBottomAnchor(imageView, 73.0);
        AnchorPane.setLeftAnchor(imageView, 11.0);
        AnchorPane.setRightAnchor(imageView, 11.0);
        imageView.setLayoutX(11.0);
        imageView.setLayoutY(32.0);

        Text descriptionText = new Text(description);
        descriptionText.setFont(new Font(10));
        descriptionText.setWrappingWidth(78.0);
        descriptionText.setLayoutX(11.0);
        descriptionText.setLayoutY(88.0);

        this.getChildren().addAll(eventText, eventNameText, imageView, descriptionText);
    }
}
