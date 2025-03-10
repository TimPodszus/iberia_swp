package de.uol.swp.client.chat.detail;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CurrentPlayerMessage extends HBox {

    public CurrentPlayerMessage(String message) {
        Label youLabel = new Label("[Du]");
        youLabel.getStyleClass()
                .add("chat-username");
        youLabel.setAlignment(Pos.TOP_CENTER);
        youLabel.setMinWidth(USE_PREF_SIZE);
        youLabel.setMaxWidth(100);

        Label messageLabel = new Label(message);
        HBox.setHgrow(messageLabel, Priority.ALWAYS);
        messageLabel.setAlignment(Pos.TOP_RIGHT);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(225);

        this.setAlignment(Pos.TOP_RIGHT);
        this.getChildren().addAll(messageLabel, youLabel);
    }
}
