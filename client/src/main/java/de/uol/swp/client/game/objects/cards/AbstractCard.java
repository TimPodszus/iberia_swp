package de.uol.swp.client.game.objects.cards;

import javafx.scene.layout.AnchorPane;
import lombok.Getter;

import java.util.Objects;

/**
 * Represents an abstract card in the game.
 */
@Getter
public class AbstractCard extends AnchorPane {
    private static final String STYLESHEET_PATH = "/css/cards.css";
    private static final String CARD_STYLE_CLASS = "card";
    private static final String CARD_SELECTED_STYLE_CLASS = "card-selected";
    /**
     * The background color for the text.
     */
    public static final String TEXT_BACKGROUND_COLOR = "-fx-background-color: white;";

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
        this.getStylesheets()
            .add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH))
                        .toExternalForm());
        this.unselect();
    }

    /**
     * Selects the card by setting the border width to the select value.
     */
    public void select() {
        this.getStyleClass()
            .removeAll(CARD_STYLE_CLASS);
        this.getStyleClass()
            .add(CARD_SELECTED_STYLE_CLASS);
    }

    /**
     * Unselects the card by setting the border width to the unselect value.
     */
    public void unselect() {
        this.getStyleClass()
            .removeAll(CARD_SELECTED_STYLE_CLASS);
        this.getStyleClass()
            .add(CARD_STYLE_CLASS);
    }
}
