package de.uol.swp.client.game.objects.dialogs;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EndGameDialog extends AbstractDialog<ButtonType> {
    private final Stage gameStage;

    public EndGameDialog(boolean hasWon, Stage gameStage) {
        this.gameStage = gameStage;

        setTitle(hasWon ? "Sie haben gewonnen" : "Sie haben verloren");
        setHeaderText(null);
        setContentText("Wählen Sie eine Option:");

        initializeDialog();
    }

    private void initializeDialog() {
        ButtonType leaveGameButton = new ButtonType("Spiel verlassen", ButtonBar.ButtonData.OK_DONE);
        ButtonType returnToGameButton = new ButtonType("Zurück zum Spiel", ButtonBar.ButtonData.CANCEL_CLOSE);

        getDialogPane().getButtonTypes().setAll(leaveGameButton, returnToGameButton);
        getDialogPane().lookupButton(leaveGameButton).getStyleClass().add(DENY_BUTTON);

        setResultConverter(dialogButton -> {
            if (dialogButton == leaveGameButton) {
                Platform.runLater(() -> gameStage.fireEvent(new WindowEvent(gameStage, WindowEvent.WINDOW_CLOSE_REQUEST)));
            }
            return dialogButton;
        });
    }
}
