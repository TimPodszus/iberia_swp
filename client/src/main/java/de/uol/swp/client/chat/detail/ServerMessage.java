package de.uol.swp.client.chat.detail;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ServerMessage extends HBox {

    public ServerMessage(String message) {
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("server-message");

        this.getChildren().add(messageLabel);
        this.getStyleClass().add("chat-server-bubble");

        this.setMaxWidth(Double.MAX_VALUE);
        this.setAlignment(javafx.geometry.Pos.CENTER);
    }
}

