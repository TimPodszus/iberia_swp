package de.uol.swp.client.chat.detail;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CurrentPlayerMessage extends HBox {

    public CurrentPlayerMessage(String message) {
        Label messageLabel = new Label(message);
        Label youLabel = new Label("Du: ");

        youLabel.getStyleClass().add("chat-username");
        messageLabel.getStyleClass().add("chat-message");

        this.getChildren().addAll(messageLabel, youLabel);
        this.getStyleClass().add("chat-bubble-right");
    }
}
