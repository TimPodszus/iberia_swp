package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.client.game.objects.PlayerButton;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.player.IPlayerDTO;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.List;

public class PlayerSelectionDialog extends AbstractDialog<String> {
    public static final String HEADER = "Spieler zum mitnehmen auswählen";
    private final List<IPlayerDTO> players;

    public PlayerSelectionDialog(List<IPlayerDTO> players, RoleEnum role) {
        super();
        this.players = players;
        super.initStyle(StageStyle.DECORATED);
        super.setHeaderText(HEADER);
        this.setContent(role);
        this.setButtons();
        this.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return super.getResult();
            } else {
                return null;
            }
        });
    }

    /**
     * Sets the content of the dialog.
     * Creates a VBox containing a label and a HBox with player buttons.
     * Each player button, when clicked, sets the selected player and the dialog result.
     */
    private void setContent(RoleEnum role) {
        VBox content = new VBox();
        Label label = new Label("Du bist " + role.getName() + " und kannst einen Spieler mitnehmen. Wähle einen " +
                "Spieler aus, den du mitnehmen möchtest.");
        HBox playerBox = new HBox();
        playerBox.getStyleClass().add(HBOX_STYLE);
        for (IPlayerDTO player : players) {
            PlayerButton playerButton = new PlayerButton(player.getUsername(), event -> {
                super.setResult(player.getUsername());
                super.getDialogPane()
                     .lookupButton(ButtonType.OK)
                     .setDisable(false);
            });
            playerBox.getChildren()
                     .add(playerButton);
        }

        content.getChildren()
               .add(label);
        content.getChildren()
               .add(playerBox);

        super.getDialogPane()
             .setContent(content);
    }

    /**
     * Sets the buttons for the dialog.
     * Adds OK and CANCEL buttons to the dialog pane.
     */
    private void setButtons() {
        super.getDialogPane()
             .getButtonTypes()
             .addAll(ButtonType.OK, ButtonType.CANCEL);
        super.getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add(APPROVE_BUTTON);
        super.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add(DENY_BUTTON);
        super.getDialogPane()
             .lookupButton(ButtonType.OK)
             .setDisable(true);
    }
}
