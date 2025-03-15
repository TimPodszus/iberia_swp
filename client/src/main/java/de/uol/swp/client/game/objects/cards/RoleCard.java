package de.uol.swp.client.game.objects.cards;

import de.uol.swp.common.game.RoleEnum;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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

        StackPane roleStackPane = createTextStackPane(role.getName(), 100.0, 10);
        StackPane descriptionStackPane = createTextStackPane(role.getDescription(), 100.0, 10);

        VBox textVBox = new VBox();
        textVBox.setPrefSize(100, 150);
        textVBox.getChildren()
                .addAll(roleStackPane, descriptionStackPane);

        this.getChildren()
            .addAll(textVBox);
    }
}