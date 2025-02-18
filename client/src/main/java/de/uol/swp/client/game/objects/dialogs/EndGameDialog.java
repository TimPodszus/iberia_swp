package de.uol.swp.client.game.objects.dialogs;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EndGameDialog {

    private final boolean hasWon;
    private final AnchorPane gameScreen;

    public EndGameDialog(boolean hasWon, AnchorPane gameScreen) {
        this.hasWon = hasWon;
        this.gameScreen = gameScreen;
    }

    public void showEndGameDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(hasWon ? "Sie haben gewonnen" : "Sie haben verloren");
        alert.setHeaderText(null);
        alert.setContentText("Wählen Sie eine Option:");

        ButtonType leaveGameButton = new ButtonType("Spiel verlassen");
        ButtonType returnToGameButton = new ButtonType("Zurück zum Spiel");

        alert.getButtonTypes().setAll(leaveGameButton, returnToGameButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == leaveGameButton) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) gameScreen.getScene().getWindow();
                    stage.close(); // Close the game scene
                });
            } else if (response == returnToGameButton) {
                alert.close(); // Close the dialog
            }
        });
    }
}