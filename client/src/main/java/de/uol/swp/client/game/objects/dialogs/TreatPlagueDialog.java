package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.infection.IInfectionDTO;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.List;

/**
 * Dialog that allows a player to choose a plague to treat.
 * This dialog presents a list of available plagues and enables the player to select one to treat.
 */
public class TreatPlagueDialog extends AbstractDialog<PlagueName> {

    private static final String HEADER = "Seuche behandeln";
    private final boolean dismissible;
    private final List<IInfectionDTO> plagues;
    private PlagueName selectedPlague;
    private final ToggleGroup group = new ToggleGroup();

    /**
     * Constructs a new TreatPlagueDialog.
     *
     * @param dismissible Indicates whether the dialog can be dismissed by the player.
     * @param plagues     The list of available plagues to choose from.
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
        VBox plagueBox = new VBox();
        plagueBox.getStyleClass().add(VBOX_STYLE);
        for (IInfectionDTO infection : this.plagues) {
            PlagueName plagueName = infection.getPlagueName();
            ToggleButton plagueButton = new ToggleButton(plagueName.getDisplayName());
            plagueButton.setToggleGroup(group);
            plagueButton.setOnAction(event -> onPlagueSelected(plagueName));

            ImageView image = new ImageView(new Image(plagueName.getImage().getPath()));
            image.setFitHeight(30);
            image.setFitWidth(30);

            HBox plagueInfo = new HBox();
            plagueInfo.getStyleClass().add(HBOX_STYLE);
            plagueInfo.getChildren()
                      .addAll(image, plagueButton);

            plagueBox.getChildren()
                     .add(plagueInfo);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(plagueBox);
        scrollPane.getStyleClass().add(SCROLL_PANE);
        scrollPane.setFitToWidth(true);
        super.getDialogPane()
             .setContent(scrollPane);
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
        Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        okButton.getStyleClass().add(APPROVE_BUTTON);
        group.selectedToggleProperty()
             .addListener((obs, oldVal, newVal) -> okButton.setDisable(newVal == null));

        if (this.dismissible) {
            super.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            super.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add(DENY_BUTTON);
        }
    }
}

