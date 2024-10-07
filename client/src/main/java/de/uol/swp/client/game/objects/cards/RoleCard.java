package de.uol.swp.client.game.objects.cards;

import de.uol.swp.common.game.RoleCardEnum;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class RoleCard extends AbstractCard {
    public RoleCard(RoleCardEnum roleCard, String role, String description) {
        this.setPrefSize(100, 150);
        this.setStyle("-fx-background-color: " + roleCard.getColorCode() + ";");

        Text roleText = new Text(role);
        AnchorPane.setTopAnchor(roleText, 7.0);
        roleText.setLayoutX(37.0);
        roleText.setLayoutY(20.0);

        Text descriptionText = new Text(description);
        descriptionText.setFont(new Font(10));
        descriptionText.setWrappingWidth(78.0);
        descriptionText.setLayoutX(11.0);
        descriptionText.setLayoutY(44.0);

        this.getChildren().addAll(roleText, descriptionText);
    }
}
