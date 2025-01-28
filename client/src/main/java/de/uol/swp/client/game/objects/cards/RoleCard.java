package de.uol.swp.client.game.objects.cards;

import de.uol.swp.common.game.RoleEnum;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Represents a role card in the game.
 * Extends the AbstractCard class.
 */
public class RoleCard extends AbstractCard {

    /**
     * Constructs a RoleCard with the specified role.
     * The id is set to -1 because role cards do not have a unique identifier and does not need one.
     *
     * @param role the role associated with the card
     */
    public RoleCard(RoleEnum role) {
        super(-1);
        this.setPrefSize(100, 150);
        this.setStyle("-fx-background-color: " + role.getColorCode() + ";");

        Text roleText = new Text(role.getName());
        StackPane roleStackPane = new StackPane();
        roleStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        roleStackPane.getChildren()
                     .add(roleText);

        AnchorPane.setTopAnchor(roleStackPane, 5.0);
        AnchorPane.setLeftAnchor(roleStackPane, 0.0);
        AnchorPane.setRightAnchor(roleStackPane, 0.0);

        Text descriptionText = new Text(role.getDescription());
        descriptionText.setFont(new Font(10));
        descriptionText.setWrappingWidth(78.0);
        descriptionText.setTextAlignment(TextAlignment.CENTER);
        StackPane descriptionStackPane = new StackPane();
        descriptionStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        descriptionStackPane.getChildren()
                            .add(descriptionText);

        AnchorPane.setTopAnchor(descriptionStackPane, 25.0);
        AnchorPane.setLeftAnchor(descriptionStackPane, 0.0);
        AnchorPane.setRightAnchor(descriptionStackPane, 0.0);

        this.getChildren()
            .addAll(roleStackPane, descriptionStackPane);
    }
}