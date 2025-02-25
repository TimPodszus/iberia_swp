package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.common.game.StateType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.concurrent.CompletableFuture;

public class TreatWaterEventDialog extends Stage {
    private static final String HEADER = "Platzierung der Wasseraufbereitung";
    private final boolean isDismissible;
    private final int waterTreatmentsLeft;
    private final StateType stateType;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final Button okButton = new Button("OK");
    private final Button exitButton = new Button("Exit");
    private final CompletableFuture<Integer> result = new CompletableFuture<>();

    public TreatWaterEventDialog(boolean isDismissible, int waterTreatmentsLeft, StateType stateType) {
        this.isDismissible = isDismissible;
        this.waterTreatmentsLeft = waterTreatmentsLeft;
        this.stateType = stateType;
        initialize();
        this.setTitle(HEADER);
    }

    private void initialize() {
        VBox vbox = new VBox(10);
        HBox buttonBox = new HBox(10);

        Label selectionLabel = new Label("Wähle die Anzahl");
        vbox.getChildren().add(selectionLabel);

        ToggleButton toggleButton1 = new ToggleButton(String.valueOf(1));
        toggleButton1.setToggleGroup(toggleGroup);
        toggleButton1.setUserData(1);
        buttonBox.getChildren()
                 .add(toggleButton1);
        if (waterTreatmentsLeft > 1 && stateType.equals(StateType.EVENT_STATE)) {
            ToggleButton toggleButton2 = new ToggleButton(String.valueOf(2));
            toggleButton2.setToggleGroup(toggleGroup);
            toggleButton2.setUserData(2);
            buttonBox.getChildren()
                     .add(toggleButton2);
        }

        okButton.setDisable(true);
        toggleGroup.selectedToggleProperty()
                   .addListener((observable, oldValue, newValue) -> okButton.setDisable(newValue == null));

        vbox.getChildren()
            .addAll(buttonBox, okButton);

        if (isDismissible) {
            exitButton.setOnAction(event -> {
                result.complete(null);
                this.close();
            });
            vbox.getChildren()
                .add(exitButton);
        }

        okButton.setOnAction(event -> {
            ToggleButton selectedButton = (ToggleButton) toggleGroup.getSelectedToggle();
            if (selectedButton != null) {
                int selectedValue = (int) selectedButton.getUserData();
                result.complete(selectedValue);
                this.close();
            }
        });

        Scene scene = new Scene(vbox, 100, 100);
        this.setScene(scene);
    }

    public CompletableFuture<Integer> showAndWaitForResult() {
        this.show();
        return result;
    }
}