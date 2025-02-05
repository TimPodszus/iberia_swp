package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.infection.IInfectionDTO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import java.util.List;

public class TreatPlagueDialog extends Dialog<PlagueName> {

    private static final String HEADER = "Plage behandeln";
    private final boolean dismissible;
    private final List<IInfectionDTO> plagues;
    private PlagueName selectedPlague;

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
     * Setzt den Inhalt des Dialogs, einschließlich der Plagen zur Auswahl.
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
     * Behandelt das Ereignis, wenn eine Plage ausgewählt wird.
     *
     * @param plagueName der Name der ausgewählten Plage
     */
    private void onPlagueSelected(PlagueName plagueName) {
        if (this.selectedPlague != null) {
            // Optional: Visuelle Darstellung der Auswahl zurücksetzen
        }
        this.selectedPlague = plagueName;  // Setzt die ausgewählte Plage
    }

    /**
     * Setzt die Buttons für den Dialog.
     */
    private void setButtons() {
        super.getDialogPane().getButtonTypes().add(ButtonType.OK);
        if (this.dismissible) {
            super.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        }
    }

}

