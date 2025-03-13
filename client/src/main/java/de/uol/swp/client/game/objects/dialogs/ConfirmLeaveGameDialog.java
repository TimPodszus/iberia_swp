package de.uol.swp.client.game.objects.dialogs;

import javafx.scene.control.Alert;

/**
 * Represents a confirmation dialog for leaving the game.
 * This dialog prompts the user to confirm if they really want to leave the game.
 */
public class ConfirmLeaveGameDialog extends CustomAlert {

    /**
     * Constructs a new ConfirmLeaveGameDialog with a confirmation alert type.
     * Sets the title and header text for the dialog.
     */
    public ConfirmLeaveGameDialog() {
        super(Alert.AlertType.CONFIRMATION);
        this.setTitle("Spiel verlassen");
        this.setHeaderText("Möchtest du das Spiel wirklich verlassen?");
    }
}