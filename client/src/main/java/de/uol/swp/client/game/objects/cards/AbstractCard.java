package de.uol.swp.client.game.objects.cards;

import javafx.scene.layout.AnchorPane;
import lombok.Getter;

/**
 * Represents an abstract card in the game.
 */
@Getter
public class AbstractCard extends AnchorPane {
    /**
     * The background color for the text.
     */
    public static final String TEXT_BACKGROUND_COLOR = "-fx-background-color: white;";

    private static final String BORDER_COLOR = "-fx-border-color: black;";
    private static final String BORDER_WIDTH_IDENTIFIER = "-fx-border-width: ";
    private static final int BORDER_WIDTH_SELECTED = 2;
    private static final int BORDER_WIDTH_UNSELECTED = 0;

    /**
     * The unique identifier for the card.
     */
    private final int cardId;

    /**
     * Constructs an AbstractCard with the specified card ID.
     *
     * @param cardId the unique identifier for the card
     */
    public AbstractCard(int cardId) {
        this.cardId = cardId;
        this.setStyle(BORDER_COLOR);
        this.unselect();
    }

    /**
     * Selects the card by setting the border width to the select value.
     */
    public void select() {
        this.setStyle(BORDER_WIDTH_IDENTIFIER + BORDER_WIDTH_SELECTED + ";");
    }

    /**
     * Unselects the card by setting the border width to the unselect value.
     */
    public void unselect() {
        this.setStyle(BORDER_WIDTH_IDENTIFIER + BORDER_WIDTH_UNSELECTED + ";");
    }
}
