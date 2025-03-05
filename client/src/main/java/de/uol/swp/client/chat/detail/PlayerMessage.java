package de.uol.swp.client.chat.detail;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class PlayerMessage extends HBox {

    public PlayerMessage(String username, String message) {
        Label nameLabel = new Label(username + ": ");
        Label messageLabel = new Label(message);

        nameLabel.getStyleClass().add("chat-username");
        messageLabel.getStyleClass().add("chat-message");

        this.getChildren().addAll(nameLabel, messageLabel);
        this.getStyleClass().add("chat-bubble");
    }
}
