package de.uol.swp.client.chat.detail;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ServerMessage extends HBox {

    public ServerMessage(String message) {
        Text messageText = new Text(message);

        messageText.setFont(Font.font("Arial", 14));
        messageText.setStyle("-fx-font-weight: bold;");
        messageText.setTextAlignment(TextAlignment.CENTER);
        messageText.wrappingWidthProperty()
                   .bind(this.widthProperty()
                             .subtract(20));

        this.setAlignment(Pos.CENTER);
        this.getChildren()
            .add(messageText);
    }
}

