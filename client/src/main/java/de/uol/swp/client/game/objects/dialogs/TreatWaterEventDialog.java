package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.common.game.StateType;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TreatWaterEventDialog extends AbstractDialog<Integer> {
    private static final String HEADER = "Platzierung der Wasseraufbereitung";
    private final int waterTreatmentsLeft;
    private final StateType stateType;
    private final boolean isDismissible;
    private final ToggleGroup toggleGroup = new ToggleGroup();

    public TreatWaterEventDialog(boolean isDismissible, int waterTreatmentsLeft, StateType stateType) {
        this.waterTreatmentsLeft = waterTreatmentsLeft;
        this.stateType = stateType;
        this.isDismissible = isDismissible;

        setHeaderText(HEADER);

        initializeDialog();
        initializeToggleButtons();
        setDialogContent();
        setDialogButtons();
        initializeToggleGroup();
    }

    private void initializeDialog() {
        getDialogPane().setContent(new VBox(5));
    }

    private void initializeToggleButtons() {
        HBox buttonBox = new HBox();
        buttonBox.getStyleClass().add(HBOX_STYLE);

        ToggleButton toggleButton1 = new ToggleButton("1");
        toggleButton1.setToggleGroup(toggleGroup);
        toggleButton1.setUserData(1);
        buttonBox.getChildren().add(toggleButton1);

        if (waterTreatmentsLeft > 1 && stateType.equals(StateType.EVENT_STATE)) {
            ToggleButton toggleButton2 = new ToggleButton("2");
            toggleButton2.setToggleGroup(toggleGroup);
            toggleButton2.setUserData(2);
            buttonBox.getChildren().add(toggleButton2);
        }

        ((VBox) getDialogPane().getContent()).getChildren().add(buttonBox);
    }

    private void setDialogContent() {
        Label selectionLabel = new Label("Wähle die Anzahl");
        ((VBox) getDialogPane().getContent()).getChildren().add(0, selectionLabel);
    }

    private void setDialogButtons() {
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add(APPROVE_BUTTON);
        if (isDismissible) {
            getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add(DENY_BUTTON);
        }
    }

    private void initializeToggleGroup() {
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) ->
                okButton.setDisable(newVal == null)
        );

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                ToggleButton selectedButton = (ToggleButton) toggleGroup.getSelectedToggle();
                return (selectedButton != null) ? (int) selectedButton.getUserData() : null;
            }
            return null;
        });
    }
}
