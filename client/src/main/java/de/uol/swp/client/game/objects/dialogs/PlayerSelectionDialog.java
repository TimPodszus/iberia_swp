package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.client.game.objects.PlayerButton;
import de.uol.swp.common.player.IPlayerDTO;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.List;

public class PlayerSelectionDialog extends Dialog<String> {
    public static final String HEADER = "Spieler zum mitnehmen auswählen";
    public static final String CONTENT = "Du bist Seemann und kannst einen Spieler mitnehmen. Wähle einen Spieler aus, den du mitnehmen möchtest.";
    private final List<IPlayerDTO> players;

    public PlayerSelectionDialog(List<IPlayerDTO> players) {
        super();
        this.players = players;
        super.initStyle(StageStyle.DECORATED);
        super.setHeaderText(HEADER);
        this.setContent();
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
    private void setContent() {
        VBox content = new VBox();
        Label label = new Label(CONTENT);
        HBox playerBox = new HBox();
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
        super.getDialogPane()
             .lookupButton(ButtonType.OK)
             .setDisable(true);
    }
}
