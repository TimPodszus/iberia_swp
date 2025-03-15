package de.uol.swp.client.chat.detail;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class CurrentPlayerMessage extends HBox {

    public CurrentPlayerMessage(String message) {
        VBox youContainer = new VBox();
        Text youText = new Text("[Du]");
        youText.getStyleClass()
                .add("chat-username");
        youContainer.setMaxWidth(50);
        youContainer.getChildren()
                    .add(youText);
        youContainer.setAlignment(Pos.TOP_CENTER);

        VBox messageContainer = new VBox();
        Text messageText = new Text(message);
        messageText.setTextAlignment(TextAlignment.RIGHT);
        messageText.wrappingWidthProperty()
                   .bind(this.widthProperty()
                             .subtract(50));
        messageContainer.getChildren()
                        .add(messageText);
        messageContainer.setAlignment(Pos.TOP_RIGHT);

        this.setSpacing(8);
        this.setAlignment(Pos.TOP_RIGHT);
        this.getChildren()
            .addAll(messageContainer, youContainer);
    }
}
