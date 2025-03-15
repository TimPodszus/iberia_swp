package de.uol.swp.client.game.objects;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import lombok.Getter;

/**
 * A custom button representing a player.
 * It displays the player's username and handles action events.
 */
@Getter
public class PlayerButton extends Button {
    private final String username;

    /**
     * Constructs a PlayerButton with the specified username and event handler.
     *
     * @param username     the username to display on the button
     * @param eventHandler the event handler to handle button actions
     */
    public PlayerButton(String username, EventHandler<ActionEvent> eventHandler) {
        this.username = username;
        this.setText(username);
        this.setMnemonicParsing(false);
        this.setPrefHeight(40.0);
        this.setPrefWidth(80.0);
        this.getStyleClass()
            .add("action-button");
        this.setUserData(1);
        this.setOnAction(eventHandler);

        HBox.setMargin(this, new javafx.geometry.Insets(5.0, 5.0, 5.0, 5.0));
    }
}
