package de.uol.swp.client.chat.detail;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ServerMessage extends HBox {

    public ServerMessage(String message) {
        Label messageLabel = new Label(message);

        this.getChildren().add(messageLabel);
        HBox.setHgrow(messageLabel, Priority.ALWAYS);

        this.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setAlignment(javafx.geometry.Pos.CENTER);
    }
}

