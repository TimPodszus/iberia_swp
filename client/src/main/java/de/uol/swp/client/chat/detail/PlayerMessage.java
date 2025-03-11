package de.uol.swp.client.chat.detail;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class PlayerMessage extends HBox {

    public PlayerMessage(String sender, String message) {
        VBox nameContainer = new VBox();
        Text nameText = new Text(sender);
        nameText.getStyleClass()
                .add("chat-username");
        nameContainer.setMaxWidth(100);
        nameContainer.getChildren()
                     .add(nameText);
        nameContainer.setAlignment(Pos.TOP_CENTER);

        VBox messageContainer = new VBox();
        Text messageText = new Text(message);
        messageText.setTextAlignment(TextAlignment.LEFT);
        messageText.wrappingWidthProperty()
                   .bind(this.widthProperty()
                             .subtract(100));
        messageContainer.getChildren()
                        .add(messageText);
        messageContainer.setAlignment(Pos.TOP_LEFT);

        this.setSpacing(8);
        this.setAlignment(Pos.TOP_LEFT);
        this.getChildren()
            .addAll(nameContainer, messageContainer);
    }
}
