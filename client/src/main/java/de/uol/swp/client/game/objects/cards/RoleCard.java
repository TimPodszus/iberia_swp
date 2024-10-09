package de.uol.swp.client.game.objects.cards;

import de.uol.swp.common.game.RoleCardEnum;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class RoleCard extends AbstractCard {
    public RoleCard(RoleCardEnum roleCard, String role, String description) {
        this.setPrefSize(100, 150);
        this.setStyle("-fx-background-color: " + roleCard.getColorCode() + ";");

        Text roleText = new Text(role);
        StackPane roleStackPane = new StackPane();
        roleStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        roleStackPane.getChildren().add(roleText);

        AnchorPane.setTopAnchor(roleStackPane, 5.0);
        AnchorPane.setLeftAnchor(roleStackPane, 0.0);
        AnchorPane.setRightAnchor(roleStackPane, 0.0);

        Text descriptionText = new Text(description);
        descriptionText.setFont(new Font(10));
        descriptionText.setWrappingWidth(78.0);
        descriptionText.setTextAlignment(TextAlignment.CENTER);
        StackPane descriptionStackPane = new StackPane();
        descriptionStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        descriptionStackPane.getChildren().add(descriptionText);

        AnchorPane.setTopAnchor(descriptionStackPane, 25.0);
        AnchorPane.setLeftAnchor(descriptionStackPane, 0.0);
        AnchorPane.setRightAnchor(descriptionStackPane, 0.0);

        this.getChildren().addAll(roleStackPane, descriptionStackPane);
    }
}
