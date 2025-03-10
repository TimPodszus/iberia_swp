package de.uol.swp.client.chat.detail;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ServerMessage extends HBox {

    public ServerMessage(String message) {
        Label messageLabel = new Label(message);

        messageLabel.setFont(Font.font("Arial", 14));
        messageLabel.setStyle("-fx-font-weight: bold;");
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setWrapText(true);

        this.setAlignment(Pos.CENTER);
        this.getChildren().add(messageLabel);
        this.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setMaxWidth(800);
    }
}

