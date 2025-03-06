package de.uol.swp.client.chat.detail;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class PlayerMessage extends HBox {

    public PlayerMessage(String username, String message) {
        Label nameLabel = new Label("[" + username + "]");
        nameLabel.getStyleClass()
                 .add("chat-username");
        nameLabel.setAlignment(Pos.TOP_CENTER);
        nameLabel.setMinWidth(USE_PREF_SIZE);
        nameLabel.setMaxWidth(100);

        Label messageLabel = new Label(message);
        HBox.setHgrow(messageLabel, Priority.ALWAYS);
        messageLabel.setAlignment(Pos.TOP_LEFT);
        messageLabel.setWrapText(true);

        this.setAlignment(Pos.TOP_LEFT);
        this.getChildren().addAll(nameLabel, messageLabel);
    }
}
