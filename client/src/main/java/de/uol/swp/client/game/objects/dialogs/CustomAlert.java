package de.uol.swp.client.game.objects.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CustomAlert extends AbstractDialog<ButtonType> {
    private final Alert.AlertType type;

    public CustomAlert(Alert.AlertType type) {
        this.type = type;
        initializeDialog();
    }

    private void initializeDialog() {
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add(APPROVE_BUTTON);
        if (type == Alert.AlertType.CONFIRMATION) {
            getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add(DENY_BUTTON);
        }
    }
}
