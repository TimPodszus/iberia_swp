package de.uol.swp.client.game.objects.cards;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.Getter;

import java.util.Objects;

/**
 * Represents an abstract card in the game.
 */
@Getter
public class AbstractCard extends Pane {
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
        this.setMouseTransparent(false);
    }

    /**
     * Creates a StackPane containing a text element with specified properties.
     * <p>
     * This method creates a StackPane with a Text element inside it, sets the font size, wrapping width,
     * and text alignment for the Text element, and applies a background color style to the StackPane.
     *
     * @param text     the text to be displayed
     * @param width    the width of the StackPane
     * @param fontSize the font size of the text
     * @return a StackPane containing the styled text element
     */
    protected StackPane createTextStackPane(String text, double width, int fontSize) {
        Text textField = new Text(text);
        textField.setFont(new Font(fontSize));
        textField.setWrappingWidth(width - 10);
        textField.setTextAlignment(TextAlignment.CENTER);
        StackPane textStackPane = new StackPane();
        textStackPane.setStyle(TEXT_BACKGROUND_COLOR);
        textStackPane.getChildren()
                     .add(textField);
        VBox.setMargin(textStackPane, new Insets(5, 0, 5, 0));

        return textStackPane;
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
