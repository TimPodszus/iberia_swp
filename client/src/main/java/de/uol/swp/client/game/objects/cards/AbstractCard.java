package de.uol.swp.client.game.objects.cards;

import javafx.scene.layout.AnchorPane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents an abstract card in the game.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AbstractCard extends AnchorPane {
    /**
     * The background color for the text.
     */
    public static final String TEXT_BACKGROUND_COLOR = "-fx-background-color: white;";

    /**
     * The unique identifier for the card.
     */
    private int cardId;
}
