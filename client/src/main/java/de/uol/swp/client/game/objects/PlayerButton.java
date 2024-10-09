package de.uol.swp.client.game.objects;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class PlayerButton extends Button {
    public PlayerButton(String player, EventHandler<ActionEvent> eventHandler) {
        this.setText(player);
        this.setMnemonicParsing(false);
        this.setPrefHeight(40.0);
        this.getStyleClass()
            .add("action-button");
        this.setUserData(1);

        this.setOnAction(eventHandler);
    }
}
