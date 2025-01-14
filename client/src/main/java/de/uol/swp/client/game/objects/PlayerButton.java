package de.uol.swp.client.game.objects;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class PlayerButton extends Button {
    public PlayerButton(String player, EventHandler<ActionEvent> eventHandler) {
        this.setText(player);
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
