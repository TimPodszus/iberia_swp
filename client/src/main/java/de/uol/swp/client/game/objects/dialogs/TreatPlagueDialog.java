package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.infection.IInfectionDTO;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;

import java.util.List;

/**
 * Dialog that allows a player to choose a plague to treat.
 * This dialog presents a list of available plagues and enables the player to select one to treat.
 */
public class TreatPlagueDialog extends Dialog<PlagueName> {

    private static final String HEADER = "Treat Plague";
    private final boolean dismissible;
    private final List<IInfectionDTO> plagues;
    private PlagueName selectedPlague;

    /**
     * Constructs a new TreatPlagueDialog.
     *
     * @param dismissible Indicates whether the dialog can be dismissed by the player.
     * @param plagues The list of available plagues to choose from.
     */
    public TreatPlagueDialog(boolean dismissible, List<IInfectionDTO> plagues) {
        this.dismissible = dismissible;
        this.plagues = plagues;
        super.initStyle(StageStyle.DECORATED);
        super.setHeaderText(HEADER);
        this.setContent();
        this.setButtons();
        super.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return this.selectedPlague;
            } else {
                return null;
            }
        });
    }

    /**
     * Sets up the content of the dialog, which includes buttons for each available plague.
     */
    private void setContent() {
        HBox plagueBox = new HBox();
        for (IInfectionDTO infection : this.plagues) {
            PlagueName plagueName = infection.getPlagueName();
            Button plagueButton = new Button(plagueName.name());
            plagueButton.setOnAction(event -> onPlagueSelected(plagueName));
            plagueBox.getChildren().add(plagueButton);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(plagueBox);
        super.getDialogPane().setContent(scrollPane);
    }

    /**
     * Updates the selected plague when a plague button is clicked.
     *
     * @param plagueName The name of the selected plague.
     */
    private void onPlagueSelected(PlagueName plagueName) {
        this.selectedPlague = plagueName;
    }

    /**
     * Sets up the buttons in the dialog (OK and optionally Cancel button).
     */
    private void setButtons() {
        super.getDialogPane().getButtonTypes().add(ButtonType.OK);
        if (this.dismissible) {
            super.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        }
    }

}

